.PHONY: help clean compile test run package install

# Maven wrapper script for MINGW/Git Bash
SHELL := /bin/bash
MVN := bash mvn.sh

help: ## Show this help message
	@echo "JavaYog - Makefile commands"
	@echo ""
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2}'

gitpush: ## git push m=any message
	clear;
	git add .; git commit -m "jy:$(m)"; git push;


clean: ## Clean build artifacts
	$(MVN) clean

compile: ## Compile the project
	$(MVN) compile

test: ## Run tests
	$(MVN) test

run: ## Run the application
	$(MVN) spring-boot:run

package: ## Package application as JAR
	$(MVN) package

install: ## Install dependencies
	$(MVN) install

build: ## Clean and package
	$(MVN) clean package

verify: ## Run tests and verify
	$(MVN) verify

.DEFAULT_GOAL := help
