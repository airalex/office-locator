(ns locator.solution)

(defn cost [s {:keys [n a b]}]
  (->> (for [i (range n)
             j (range n)]
         [i j])
       (reduce (fn [acc [i j]]
                 (+ acc
                    (* (get-in a [i j])
                       (get-in b [(get s i) (get s j)]))))
               0)))


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


(defn gen-neighbor [s]
  (let [[i1 i2] (random-ints-pair (count s))]
    (swap-positions s i1 i2)))

(defn gen-random [n]
  (shuffle (range n)))
