(ns locator.main
  (:require [locator.qaplib :as qaplib]
            [locator.sheet :as sheet]
            [locator.sim-anneal :as sim-anneal]
            [locator.solution :as solution]))


(defn -main []
  (println "hello!"))

(comment
  (-> (let [problem (qaplib/parse-qaplib-problem "qapdata/nug20.dat")
            solution (sim-anneal/run problem 1000 0.001)
            cost (solution/cost solution problem)]
        {:best solution
         :cost cost})
      (println))

  (-> (let [problem (qaplib/parse-qaplib-problem "lekta/hardcoded.dat")
            solution (sim-anneal/run problem 1000 0.001)
            cost (solution/cost solution problem)]
        {:best solution
         :cost cost})
      (println))

  (-> (let [problem (sheet/read-problem)
            solution (sim-anneal/run problem 1000 0.001)
            cost (solution/cost solution problem)]
        {:best solution
         :cost cost})
      (println))

  )
