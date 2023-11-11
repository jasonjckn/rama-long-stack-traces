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
      (source> *depot :> *sentence)
      (str/split *sentence #"\s+" :> *words)
      (r.ops/explode *words :> *word)
      (|hash *word)
      (+compound $$word->count {*word (r.aggs/+count)} )
      (printf "'%s', " *word))))

(defn -main [& args ]
  (defonce ipc (r.test/create-ipc))
  (try
    (r.test/destroy-module! ipc (get-module-name MyModule))
    (catch Exception _))


  (r.test/launch-module! ipc MyModule {:tasks 2 :threads 2})
  (def depot (foreign-depot ipc (get-module-name MyModule) "*depot"))

  (def $$word->count (foreign-pstate ipc (get-module-name MyModule) "$$word->count"))


  (println "\n--------------------------")
  (doseq [w [:a :a :a :b :c]]
    (foreign-append! depot w))

  (pr (foreign-select :a $$word->count))






  )

#_ (defonce ipc (r.test/create-ipc))

#_ (do
     (try
       (r.test/destroy-module! ipc (get-module-name MyModule))
       (catch Exception _))


     (r.test/launch-module! ipc MyModule {:tasks 2 :threads 2})
     (def depot (foreign-depot ipc (get-module-name MyModule) "*depot"))

     (def $$word->count (foreign-pstate ipc (get-module-name MyModule) "$$word->count"))





     (println "\n--------------------------")
     (doseq [w [:a :a :a :b :c]]
       (foreign-append! depot w))

     (pr (foreign-select :a $$word->count))





     )
