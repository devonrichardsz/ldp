#include <chrono>
#include <list>
#include <new>
#include <type_traits>

#include <experimental/coroutine>

template<class T>
struct generator {
    struct promise_type {
        T current_value_;
        auto yield_value(T value) {
            this->current_value_ = value;
            return std::experimental::suspend_always{};
        }
        auto initial_suspend() { return std::experimental::suspend_always{}; }
        auto final_suspend() { return std::experimental::suspend_always{}; }
        generator get_return_object() { return generator{this}; };
        void unhandled_exception() { std::terminate(); }
        void return_void() {}
    };

    using handle_t = std::experimental::coroutine_handle<promise_type>;

    struct iterator {
        handle_t coro_;
        bool done_;

        iterator() : done_(true) {}

        explicit iterator(handle_t coro, bool done)
            : coro_(coro), done_(done) {}

        iterator& operator++() {
            coro_.resume();
            done_ = coro_.done();
            return *this;
        }

        bool operator==(const iterator& rhs) const { return done_ == rhs.done_; }
        bool operator!=(const iterator& rhs) const { return !(*this == rhs); }
        const T& operator*() const { return coro_.promise().current_value_; }
        const T *operator->() const { return &*(*this); }
    };

    iterator begin() {
        coro_.resume();
        return iterator(coro_, coro_.done());
    }

    iterator end() { return iterator(); }

    generator(const generator&) = delete;
    generator(generator&& rhs) : coro_(std::exchange(rhs.coro_, nullptr)) {}

    ~generator() {
        if (coro_) {
            coro_.destroy();
        }
    }

private:
    explicit generator(promise_type *p)
        : coro_(handle_t::from_promise(*p)) {}

    handle_t coro_;
};

template <typename T>
struct type_value {};

template <size_t size, size_t align>
class initializing_buffer {
private:
    union type
    {
        std::byte __data[size];
#ifdef _MSC_VER
        struct __declspec(align(align)) {} __align;
#else // _MSC_VER
        struct __attribute__((__aligned__((align)))) { } __align;
#endif // _MSC_VER
    } value;

    using deletePtrType = void(*)(void*);
    deletePtrType deletePtr;
    void* selfPtr;

public:
    constexpr static size_t size_v = size;
    constexpr static size_t align_v = align;
    template <typename T, typename... Args> requires (sizeof(T) <= size && alignof(T) <= align && align % alignof(T) == 0)
    initializing_buffer(type_value<T>, Args&&... args)
            : deletePtr([](void* memory){ delete static_cast<T*>(memory);}), selfPtr(value.__data) {
        new (value.__data) T{std::forward<Args>(args)...};
    }

    template <typename T, typename... Args> requires (sizeof(T) > size || alignof(T) > align || align % alignof(T) != 0)
    initializing_buffer(type_value<T>, Args&&... args)
            : deletePtr([](void* memory){ delete static_cast<T*>(memory); }), selfPtr(new T{std::forward<Args>(args)...})
    { }

    initializing_buffer(void*(*copyPtr)(const void*, void*), const initializing_buffer& other)
        : deletePtr(other.deletePtr), selfPtr(copyPtr(other, value.__data))
    { }
    
    initializing_buffer(void*(*movePtr)(void*, void*), initializing_buffer&& other)
        : deletePtr(other.deletePtr), selfPtr(movePtr(other, value.__data))
    { }

    initializing_buffer(const initializing_buffer& other) = delete;
    initializing_buffer(initializing_buffer&& other) = delete;

    constexpr operator void*() { // NOLINT(google-explicit-constructor,hicpp-explicit-conversions)
        return selfPtr;
    }

    constexpr operator const void*() const { // NOLINT(google-explicit-constructor,hicpp-explicit-conversions)
        return selfPtr;
    }

    ~initializing_buffer() {
        deletePtr((void*)(*this));
    }
};

template<template<typename> typename... Features>
class copyable_erased : public Features<copyable_erased<Features...>>..., public initializing_buffer<8, 8> {
    void*(*copyConstructPtr)(const void*, void*);
    void*(*moveConstructPtr)(void*, void*);

public:
    template<typename T, typename... Args>
    copyable_erased(type_value<T> t, Args&&... args)
            : initializing_buffer(t, std::forward<Args>(args)...), Features<copyable_erased<Features...>>(t)...,
              copyConstructPtr([](const void* source, void* dest) {
                if constexpr(sizeof(T) <= size_v && alignof(T) <= align_v && align_v % alignof(T) == 0) {
                    new (dest) T{*static_cast<const T*>(source)};
                    return dest;
                } else {
                    return new T{*static_cast<const T*>(source)};
                }
              }), 
              moveConstructPtr([](void* source, void* dest) {
                if constexpr(sizeof(T) <= size_v && alignof(T) <= align_v && align_v % alignof(T) == 0) {
                    new (dest) T{std::move(*static_cast<T*>(source))};
                    return dest;
                } else {
                    return new T{std::move(*static_cast<T*>(source))};
                }
              })
    { }

    copyable_erased(const copyable_erased& other) 
            : initializing_buffer(other.copyConstructPtr, other), Features<copyable_erased<Features...>>(other)...,
              copyConstructPtr(other.copyConstructPtr), moveConstructPtr(other.moveConstructPtr)
    { }

    copyable_erased(copyable_erased&& other)
            : initializing_buffer(other.moveConstructPtr, other), Features<copyable_erased<Features...>>(other)...,
              copyConstructPtr(other.copyConstructPtr), moveConstructPtr(other.moveConstructPtr)
    { }
};

struct Age {
    size_t years;
    size_t days;
};

constexpr Age PROTO_AGE{0, 0};

class AgeBuilder {
    size_t years_;
    size_t days_;
    std::chrono::steady_clock::time_point birth_date_;
    constexpr static std::byte YEARS_FLAG{1};
    constexpr static std::byte DAYS_FLAG{2};
    constexpr static std::byte BIRTH_DATE_FLAG{4};
    std::byte flags{0};

public:
    AgeBuilder& setYears(size_t years) {
        years_ = years;
        flags |= YEARS_FLAG;
        return *this;
    }

    AgeBuilder& setDays(size_t days) {
        days_ = days;
        flags |= DAYS_FLAG;
        return *this;
    }

    AgeBuilder& setBirthDate(std::chrono::steady_clock::time_point birth_date) {
        birth_date_ = birth_date;
        flags |= BIRTH_DATE_FLAG;
        return *this;
    }

    Age build() {
        if (flags == (YEARS_FLAG | DAYS_FLAG)) {
            return {years_, days_};
        } else if (flags == BIRTH_DATE_FLAG) {
            auto elapsed = std::chrono::steady_clock::now() - birth_date_;
            return {(size_t)std::chrono::duration_cast<std::chrono::years>(elapsed).count(), 
                    (size_t)std::chrono::duration_cast<std::chrono::days>(elapsed).count() % 365};
        } else {
            throw "Properties were not set in a valid configuration";
        }
    }
};

class Singleton {
    consteval Singleton() {}
    Singleton(const Singleton& other) = delete;
    Singleton(Singleton&& other) = delete;
    constinit static Singleton INSTANCE;

public:
    Singleton& get_instance() { return INSTANCE; }
};

template <typename Buffer>
class LoggerFeature {
    void(*logPtr)(void*, const std::string&);

public:
    template<typename T>
    LoggerFeature(type_value<T>) : logPtr([](void* self, const std::string& message){ static_cast<T*>(self)->log(message); })
    { }

    void log(const std::string& message) {
        logPtr(static_cast<void*>(static_cast<Buffer&>(*this)), message);
    }
};

template<template<typename> typename... Features>
using Logger = copyable_erased<LoggerFeature, Features...>;

enum struct LoggerType {
    STDOUT,
    STDERR,
};

struct StdOutLogger {
    void log(const std::string& message);
};

struct StdErrLogger {
    void log(const std::string& message);
};

Logger<> create_logger(const LoggerType type);

template <typename Buffer>
class LoggerFactoryFeature {
    Logger<>(*constructPtr)(const void*);

public:
    template<typename T>
    LoggerFactoryFeature(type_value<T>) : constructPtr([](const void* self){ return static_cast<T*>(self)->construct(); })
    { }

    Logger<> construct() const {
        return constructPtr(static_cast<const void*>(static_cast<const Buffer&>(*this)));
    }
};

template <template<typename> typename... Features>
using LoggerFactory = copyable_erased<LoggerFactoryFeature, Features...>;

struct StdOutLoggerFactory {
    Logger<> construct();
};

struct StdErrLoggerFactory {
    Logger<> construct();
};

template<typename Buffer>
class OnClickFeature {
    bool(*onClickPtr)(void*);

public:
    template<typename T>
    OnClickFeature(type_value<T>) : onClickPtr([](void* self){ return static_cast<T*>(self)->on_click(); })
    { }
    
    bool on_click() {
        return onClickPtr(static_cast<void*>(static_cast<Buffer&>(*this)));
    }
};

template <template<typename> typename... Features>
using OnClickHandler = copyable_erased<OnClickFeature, Features...>;

struct ChainableOnClick {
    OnClickHandler<> handler_;
    
    ChainableOnClick(OnClickHandler<> handler) : handler_(handler)
    { }

    void on_click(std::list<ChainableOnClick>::iterator current, const std::list<ChainableOnClick>::iterator end);
};

class ChainedOnClick {
    std::list<ChainableOnClick> handlers;

public:
    void push_back(const ChainableOnClick& handler);
    void pop_back();
    bool empty();
    void on_click();
};

generator<size_t> fibonacci();

struct ExpressionVisitor;

template <typename Buffer>
class ExpressionFeature {
    double(*evaluatePtr)(const void*);
    double(*acceptPtr)(const void*, const Expression& expression);

public:
    template<typename T>
    ExpressionFeature(type_value<T>)
            : evaluatePtr([](const void* self){ return static_cast<const T*>(self)->evaluate(); })
              acceptPtr([](const void* self, const ExpressionVisitor& visitor){ return static_cast<const T*>(self)->accept(visitor); })
    { }

    double evaluate() const {
        return evaluatePtr(static_cast<const void*>(static_cast<const Buffer&>(*this)));
    }

    double accept(const ExpressionVisitor& visitor) const {
        return acceptPtr(static_cast<const void*>(static_cast<const Buffer&>(*this)), visitor);
    }
};

template<template<typename> typename... Features>
using Expression = copyable_erased<ExpressionFeature, Features...>;

struct ConstantExpression;
struct UnaryExpression;
struct BinaryExpression;

struct ExpressionVisitor {
    double visit(const Expression& expression) const {
        return expression.accept(*this);
    }

    double visitConstant(const ConstantExpression& expression) const;

    double visitUnary(const UnaryExpression& expression) const;

    double visitBinary(const BinaryExpression& expression) const;
};

struct ConstantExpression {
    double value_;

    ConstantExpression(double value) : _value(value)
    { }

    double evaluate() const {
        return value_;
    }

    double accept(const ExpressionVisitor& visitor) const {
        return visitor.visitConstant(*this);
    }
};

struct UnaryExpression {
    std::function<double(double)> command_;
    Expression child_;

    template<typename Func>
    UnaryExpression(Func&& command, const Expression& child) : command_(std::forward<Func>(command)), child_(child)
    { }

    double evaluate() const {
        return command_(child_.evaluate());
    }

    double accept(const ExpressionVisitor& visitor) const {
        return visitor.visitUnary(*this);
    }
}

struct BinaryExpression {
    std::function<double(double, double)> command_;
    Expression left_child_;
    Expression right_child_;

    template<typename Func>
    BinaryExpression(Func&& command, const Expression& left_child, const Expression& right_child)
            : command_(std::forward<Func>(command), left_child_(left_child), right_child_(right_child)
    { }

    double evaluate() {
        return command_(left_child_.evaluate(), right_child_.evaluate());
    }

    double accept(const ExpressionVisitor& visitor) const {
        visitor.visitBinary(*this);
    }
};
