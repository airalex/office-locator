(ns locator.sim-anneal
  (:require [locator.solution :as solution]))


(defn run [problem
           initial-temp
           cooling-rate]
  (let [initial-solution (solution/gen-random (:n problem))]
    (loop [current-solution initial-solution
           temp initial-temp
           best-so-far initial-solution]
      (if (< temp 1)
        best-so-far
        (let [current-energy (solution/cost current-solution problem)
              neighbor (solution/gen-neighbor current-solution)
              neighbor-energy (solution/cost neighbor problem)
              next-temp (* temp (- 1 cooling-rate))
              determine-next-best (fn [s] (min-key #(solution/cost % problem) s best-so-far))]
          (if (< neighbor-energy current-energy)
            (recur neighbor next-temp (determine-next-best neighbor))
            (let [swap-proba (Math/exp (/ (- current-energy neighbor-energy)
                                          temp))
                  draw (rand)]
              (if (< draw swap-proba)
                (recur neighbor next-temp (determine-next-best neighbor))
                (recur current-solution next-temp (determine-next-best current-solution))))))))))

