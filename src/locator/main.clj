(ns locator.main
  (:require [locator.problem :as problem]
            [locator.sim-anneal :as sim-anneal]
            [locator.solution :as solution]))


(defn -main []
  (println "hello!"))

(comment
  (let [problem (problem/parse-qaplib-problem "qapdata/nug12.dat")
        solution (sim-anneal/run-2 problem 1000 0.001)
        cost (solution/cost solution problem)]
    {:best solution
     :cost cost})

  )
