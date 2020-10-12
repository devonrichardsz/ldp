#include <iostream>

#include "oops.h"

copyable_erased<LoggerFeature> create_logger(const LoggerType type) {
    switch(type) {
    case LoggerType::STDOUT:
        return {type_value<StdOutLogger>{}};
    case LoggerType::STDERR:
        return {type_value<StdErrLogger>{}};
    }
}

void StdOutLogger::log(const std::string& message) {
    std::cout << message << std::endl;
}

void StdErrLogger::log(const std::string& message) {
    std::cerr << message << std::endl;
}

Logger<> StdOutLoggerFactory::construct() {
    return {type_value<StdOutLogger>{}};
}

Logger<> StdErrLoggerFactory::construct() {
    return {type_value<StdErrLogger>{}};
}

void ChainableOnClick::on_click(std::list<ChainableOnClick>::iterator current, const std::list<ChainableOnClick>::iterator end) {
    current++;
    if (!handler_.on_click() && current != end) {
        current->on_click(current, end);
    }
}

void ChainedOnClick::push_back(const ChainableOnClick& handler) {
    handlers.push_back(handler);
}

void ChainedOnClick::pop_back() {
    handlers.pop_back();
}

bool ChainedOnClick::empty() {
    return handlers.empty();
}

void ChainedOnClick::on_click() {
    if (empty()) {
        handlers.front().on_click(handlers.begin(), handlers.end());
    }
}

generator<size_t> fibonacci() {
    size_t a=0;
    size_t b=1;
    co_yield a;
    co_yield b;
    while (true) {
        a = std::exchange(b, a + b);
        co_yield b;
    }
}

double ExpressionVisitor::visitConstant(const ConstantExpression& expression) const {
    return expression.value_;
}

double ExpressionVisitor::visitUnary(const UnaryExpression& expression) const {
    double child = visit(expression.child_);
    return expression.command_(child);
}

double ExpressionVisitor::visitBinary(const BinaryExpression& expression) const {
    double left_child = visit(expression.left_child_);
    double right_child = visit(expression.right_child_);
    return expression.command_(left_child, right_child);
}

int main() {
    AgeBuilder b;
    b.setYears(37);
    b.setDays(345);
    std::cout << b.build().years << std::endl;
}
