#!/usr/bin/env -S bash -Eeuxo pipefail

lein run -m rama-clojure-starter.wordcount 2> stacktrace.txt || echo

cat stacktrace.txt | wc -l
