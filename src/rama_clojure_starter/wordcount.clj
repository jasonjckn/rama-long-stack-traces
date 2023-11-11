(ns rama-clojure-starter.wordcount
  (:require
   [com.rpl.rama.test :as r.test]
   [clojure.string :as str]
   [com.rpl.rama :refer :all]
   [com.rpl.rama.path :refer :all]
   [com.rpl.rama.aggs :as r.aggs]
   [com.rpl.rama.ops :as r.ops]))

(defmodule MyModule [R R-topo]
  (declare-depot R *depot :random)
  (let [s (stream-topology R-topo "wc-topology")]
    (declare-pstate s $$word->count {String Long})

    (<<sources s
      (source> *depot :> *out)
      (println *out))))


(comment

  (defonce ipc (r.test/create-ipc))
  (do
    (try
      (r.test/destroy-module! ipc (get-module-name MyModule))
      (catch Exception _))


    (r.test/launch-module! ipc MyModule {:tasks 2 :threads 2})
    (def depot (foreign-depot ipc (get-module-name MyModule) "*depot"))

    (doseq [_ (range 10)]
      (foreign-append! depot "hello"))

    ))
