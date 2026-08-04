#include "hello.hpp"

namespace stateful {

std::string greet(const std::string& name) {
    return "Hello, " + name + "!";
}

}  // namespace stateful
