cmake_minimum_required(VERSION 3.17)

project(LDP
        VERSION 1.0.0
        DESCRIPTION "Demos for the Zemoso LDP program."
        HOMEPAGE_URL "https://github.com/devonrichardsz/ldp"
        LANGUAGES CXX)

add_executable(oops oops.cpp oops.h)

target_compile_features(oops PUBLIC cxx_std_20)
target_include_directories(oops PUBLIC extern/immer extern/PEGTL/include)
