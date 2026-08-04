#pragma once

#include <string>

namespace stateful {

// Returns a greeting for the given name.
// Placeholder for the real stateful-service C++ implementation —
// stands in for "the server is alive and buildable" during POC setup.
std::string greet(const std::string& name = "World");

}  // namespace stateful
