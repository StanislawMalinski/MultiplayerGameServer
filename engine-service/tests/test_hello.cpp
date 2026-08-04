#define DOCTEST_CONFIG_IMPLEMENT_WITH_MAIN
#include "doctest.h"

#include "hello.hpp"

TEST_CASE("greet() with default argument returns a greeting for World") {
    CHECK(stateful::greet() == "Hello, World!");
}

TEST_CASE("greet() with a custom name returns a personalized greeting") {
    CHECK(stateful::greet("Player One") == "Hello, Player One!");
}

TEST_CASE("greet() with an empty string still produces valid output") {
    CHECK(stateful::greet("") == "Hello, !");
}
