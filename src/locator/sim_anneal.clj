(ns locator.sim-anneal
  (:require [locator.solution :as solution]))


(defn run-2 [problem
             initial-temp
             cooling-rate]
  (loop [current-solution (solution/gen-random (:n problem))
         temp initial-temp]
    (if (< temp 1)
      current-solution
      (let [current-energy (solution/cost current-solution problem)
            neighbor (solution/gen-neighbor current-solution)
            neighbor-energy (solution/cost neighbor problem)
            next-temp (* temp (- 1 cooling-rate))]
        (if (< neighbor-energy current-energy)
          (recur neighbor next-temp)
          (let [swap-proba (Math/exp (/ (- current-energy neighbor-energy)
                                        temp))
                draw (rand)]
            (if (< draw swap-proba)
              (recur neighbor next-temp)
              (recur current-solution next-temp))))))))
