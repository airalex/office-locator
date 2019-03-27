(ns locator.qaplib
  (:require [clojure.java.io :as java.io]
            [clojure.string :as str]))


(defn example-problem []
  {:n 12
   :a [[0 1 2 3 1 2 3 4 2 3 4 5]
       [1 0 1 2 2 1 2 3 3 2 3 4]
       [2 1 0 1 3 2 1 2 4 3 2 3]
       [3 2 1 0 4 3 2 1 5 4 3 2]
       [1 2 3 4 0 1 2 3 1 2 3 4]
       [2 1 2 3 1 0 1 2 2 1 2 3]
       [3 2 1 2 2 1 0 1 3 2 1 2]
       [4 3 2 1 3 2 1 0 4 3 2 1]
       [2 3 4 5 1 2 3 4 0 1 2 3]
       [3 2 3 4 2 1 2 3 1 0 1 2]
       [4 3 2 3 3 2 1 2 2 1 0 1]
       [5 4 3 2 4 3 2 1 3 2 1 0]]
   :b [[0  5  2  4  1  0  0  6  2  1  1  1]
       [5  0  3  0  2  2  2  0  4  5  0  0]
       [2  3  0  0  0  0  0  5  5  2  2  2]
       [4  0  0  0  5  2  2 10  0  0  5  5]
       [1  2  0  5  0 10  0  0  0  5  1  1]
       [0  2  0  2 10  0  5  1  1  5  4  0]
       [0  2  0  2  0  5  0 10  5  2  3  3]
       [6  0  5 10  0  1 10  0  0  0  5  0]
       [2  4  5  0  0  1  5  0  0  0 10 10]
       [1  5  2  0  5  5  2  0  0  0  5  0]
       [1  0  2  5  1  4  3  5 10  5  0  2]
       [1  0  2  5  1  0  3  0 10  0  2  0]]})



(defn parse-qaplib-problem [resource-path]
  (let [contents (slurp (java.io/resource resource-path))
        all-nums (->> (re-seq #"[\d.]+" contents)
                      (map #(Integer/parseInt %)))
        [[n] nums] (split-at 1 all-nums)
        [as bs] (split-at (* n n) nums)
        a (->> (partition n as)
               (map vec)
               vec)
        b (->> (partition n bs)
               (map vec)
               vec)]
    {:n n
     :a a
     :b b}))


(defn read-team-members []
  (-> (slurp (java.io/resource "lekta/team_members.txt"))
      (str/split-lines)))

(defn read-desks []
  (-> (slurp (java.io/resource "lekta/desks.txt"))
      (str/split-lines)))

(defn interpret-solution [s names desks]
  (map-indexed (fn [member-i desk-i]
                 [(get names member-i) (get desks desk-i)])
               s))

(comment
  (-> (parse-qaplib-problem "qapdata/bur26a.dat")
      )

  (read-team-members)

  (read-desks)

  (-> (interpret-solution [14 6 7 3 9 2 1 5 0 4 20 8 23 24 12 22 26 27 10 11 25 19 13 16 15 18 21 17]
                      (read-team-members)
                      (read-desks))
      (println))


  )
