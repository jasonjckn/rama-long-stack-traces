(ns rama-clojure-starter.my-module
  (:require
   [com.rpl.rama.test :as r.test]
   [clojure.string :as str]
   [com.rpl.rama :refer :all]
   [com.rpl.rama.path :refer :all]
   [com.rpl.rama.aggs :as r.aggs]
   [com.rpl.rama.ops :as r.ops]))

(defmodule MyModule [R R-topo]
  (declare-depot R *depot :random)
  (let [s (stream-topology R-topo "hello-topology")]
    (<<sources s
      (source> *depot :> *out)
      (println *out))))


(defonce ipc (r.test/create-ipc))

(try
  (r.test/destroy-module! ipc (get-module-name MyModule))
  (catch Exception _))

(r.test/launch-module! ipc MyModule {:tasks 2 :threads 2})
(def depot (foreign-depot ipc (get-module-name MyModule) "*depot"))

(doseq [i (range 10)]
  (foreign-append! depot (str "Hello! " i)))
