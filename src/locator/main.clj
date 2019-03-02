(ns locator.main
  (:require [locator.problem :as problem]))

(defn example-solution []
  [5 4 0 9 10 8 6 1 3 11 2 7])


(defn swap-positions [v i1 i2]
  (-> v
      (assoc i1 (nth v i2))
      (assoc i2 (nth v i1))))

(defn random-ints-pair [n]
  (let [i1 (rand-int n)
        i2 (rand-int (dec n))]
    (if (< i2 i1)
      [i1 i2]
      [i1 (inc i2)])))


(defn gen-neighbor-solution [s]
  (let [[i1 i2] (random-ints-pair (count s))]
    (swap-positions s i1 i2)))

(defn energy [s {:keys [n a b]}]
  (->> (for [i (range n)
             j (range n)]
         [i j])
       (reduce (fn [acc [i j]]
                 (+ acc
                    (* (get-in a [i j])
                       (get-in b [(get s i) (get s j)]))))
               0)))

(defn simulated-annealing [problem
                           initial-solution
                           initial-temp
                           cooling-rate]
  (loop [current-solution initial-solution
         temp initial-temp]
    (if (< temp 1)
      current-solution
      (let [current-energy (energy current-solution problem)
            neighbor (gen-neighbor-solution current-solution)
            neighbor-energy (energy neighbor problem)
            next-temp (* temp (- 1 cooling-rate))]
        (if (< neighbor-energy current-energy)
          (recur neighbor next-temp)
          (let [swap-proba (Math/exp (/ (- current-energy neighbor-energy)
                                        temp))
                draw (rand)]
            (if (< draw swap-proba)
              (recur neighbor next-temp)
              (recur current-solution next-temp))))))))


(defn -main []
  (println "hello!"))

(comment
  (energy (example-solution) (problem/example-problem))

  (gen-neighbor-solution [0 1 2 3])

  (let [problem (problem/example-problem)
        solution (simulated-annealing problem
                                      (example-solution)
                                      1000
                                      0.001)
        energy (energy solution problem)]
    [solution energy])

  )
