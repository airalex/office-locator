(ns locator.main
  (:require [locator.problem :as problem]))

(defn example-solution []
  [5 4 0 9 10 8 6 1 3 11 2 7])

(defn -main []
  (println "hello!"))


(defn swap-positions [v i1 i2]
  (-> v
      (assoc i1 (nth v i2))
      (assoc i2 (nth v i1))))

(defn energy [s {:keys [n a b]}]
  (->> (for [i (range n)
             j (range n)]
         [i j])
       (reduce (fn [acc [i j]]
                 (+ acc
                    (* (get-in a [i j])
                       (get-in b [(get s i) (get s j)]))))
               0)))

(comment
  (+ 1 2)

  (-> (shuffle (range 12))
      println)

  (swap-positions (vec (range 10)) 2 4)

  (energy (example-solution) (problem/example-problem))

  )
