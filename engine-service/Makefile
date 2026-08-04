CXX      := g++
CXXSTD   := -std=c++17
WARNINGS := -Wall -Wextra -Wpedantic
CXXFLAGS := $(CXXSTD) $(WARNINGS) -Isrc -Ithird_party

SRC_DIR   := src
TEST_DIR  := tests
BUILD_DIR := out

APP_BIN   := $(BUILD_DIR)/engine-service
TEST_BIN  := $(BUILD_DIR)/run_tests

# Sources shared between the app and the tests (everything except main.cpp)
LIB_SRCS  := $(filter-out $(SRC_DIR)/main.cpp, $(wildcard $(SRC_DIR)/*.cpp))
APP_SRCS  := $(SRC_DIR)/main.cpp $(LIB_SRCS)
TEST_SRCS := $(wildcard $(TEST_DIR)/*.cpp) $(LIB_SRCS)

.PHONY: all build run test clean

all: build

build: $(APP_BIN)

$(APP_BIN): $(APP_SRCS) | $(BUILD_DIR)
	$(CXX) $(CXXFLAGS) -o $@ $(APP_SRCS)

run: build
	./$(APP_BIN)

test: $(TEST_BIN)
	./$(TEST_BIN)

$(TEST_BIN): $(TEST_SRCS) | $(BUILD_DIR)
	$(CXX) $(CXXFLAGS) -o $@ $(TEST_SRCS)

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

clean:
	rm -rf $(BUILD_DIR)
