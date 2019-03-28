(ns locator.solution)

(defn objects-locations-cost [s {:keys [n a b]}]
  (->> (for [i (range n)
             j (range n)]
         [i j])
       (reduce (fn [acc [i j]]
                 (+ acc
                    (* (get-in a [i j])
                       (get-in b [(get s i) (get s j)]))))
               0)))

(defn cost [s problem]
  (objects-locations-cost s problem))


(defn objects-locations-preferences-cost [s {:keys [n a b c]}]
  (->> (for [i (range n)
             j (range n)]
         [i j])
       (reduce (fn [acc [i j]]
                 (let [flow (get-in a [i j])
                       distance (get-in b [(get s i) (get s j)])
                       preference (get-in c [i (get s i)])]
                   (+ acc
                    (/ (* flow distance)
                       preference)) ))
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


(defn interpret-solution [s
                          {:keys [a-labels b-labels]}]
  (map-indexed (fn [member-i desk-i]
                 [(nth a-labels member-i) (nth b-labels desk-i)])
               s))


(comment
  (interpret-solution [13 11 10 6 3 9 4 16 12 1 14 0 18 17 8 21 19 15 2 5 7 20]
                      {:a-labels '("Agnieszka"
                                   "Alex"
                                   "Arek"
                                   "Dawid"
                                   "Jakub"
                                   "Joanna"
                                   "Karolina"
                                   "Katarzyna K."
                                   "Katarzyna O."
                                   "Klaudia"
                                   "Marcin B."
                                   "Marcin Z."
                                   "Martyna"
                                   "Mateusz P."
                                   "Matt B."
                                   "Michał"
                                   "Norbert"
                                   "Paweł"
                                   "Piotr"
                                   "Stanisław"
                                   "Tomasz"
                                   "Wojciech")
                       :b-labels '("1"
                                   "2"
                                   "3"
                                   "4"
                                   "5"
                                   "6"
                                   "7"
                                   "8"
                                   "9"
                                   "10"
                                   "11"
                                   "12"
                                   "13"
                                   "14"
                                   "15"
                                   "16"
                                   "17"
                                   "18"
                                   "19"
                                   "20"
                                   "21"
                                   "22")})

  )
