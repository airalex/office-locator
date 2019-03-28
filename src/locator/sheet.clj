(ns locator.sheet
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.set :as set]))


(defn csv-rows->maps [rows]
  (map zipmap
       (->> (first rows)
            (repeat))
       (rest rows)))

(defn sheet->indexed-df [rows]
  (let [col-names (-> (first rows)
                      (rest))
        cells (->> (rest rows)
                   (map (fn [[row-name & values]]
                          (map-indexed (fn [val-i val]
                                         (let [col-name (nth col-names val-i)]
                                           [{:row row-name
                                             :col col-name}
                                            (Integer/parseInt val)]))
                                       values)))
                   (apply concat)
                   (into {}))]
    {:indexed-cells cells
     :col-names col-names
     :row-names (->> (rest rows)
                     (map first))}))

(defn parse-tm-tm-rows [rows]
  (let [{colleagues :col-names
         respondents :row-names
         cells :indexed-cells} (sheet->indexed-df rows)
        _ (assert (set/subset? (set respondents)
                               (set colleagues)))
        all-labels (-> (set/union (set colleagues) (set respondents))
                       sort)]
    {:indexed-cells cells
     :col-names all-labels
     :row-names all-labels}))

(def required-tm-names
  #{"Agnieszka"
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
     "Wojciech"})

(defn parse-tm-s-rows [rows]
  (let [df (sheet->indexed-df rows)]
    (update df :row-names #(-> (into required-tm-names %)
                               sort))))

(defn carthesian-dist [{x1 :x, y1 :y}
                       {x2 :x, y2 :y}]
  (let [dx (- x2 x1)
        dy (- y2 y1)]
    (Math/sqrt (+ (* dx dx)
                  (* dy dy)))))

(defn parse-spot-positions [rows]
  (let [points (->> (rest rows)
                    (map (fn [[name x y]]
                           {:x (Integer/parseInt x)
                            :y (Integer/parseInt y)
                            :name name})))
        names (-> (map :name points)
                  (sort)
                  (vec))
        cells (->> (for [p1 points]
                     (for [p2 points]
                       [{:row (:name p1)
                         :col (:name p2)}
                        (carthesian-dist p1 p2)]))
                   (apply concat)
                   (into {}))]
    {:indexed-cells cells
     :col-names names
     :row-names names}))


(defn transpose [rows]
  (apply map vector rows))

(defn indexed-df->rect-df [{:keys [indexed-cells
                                   col-names
                                   row-names]}
                           & {:keys [missing-fill]}]
  (let [vals-cols (reduce (fn [matrix-acc col-name]
                            (conj (vec matrix-acc)
                                  (reduce (fn [col-acc row-name]
                                            (conj col-acc
                                                  (get indexed-cells {:row row-name
                                                                      :col col-name} missing-fill)))
                                          [] row-names)))
                          [] col-names)]
    {:cell-vals (vec (transpose vals-cols))
     :col-names col-names
     :row-names row-names}))


(defn read-spot-positions-df []
  (let [path (io/resource "responses/Jak skutecznie miejsca (Responses) - Spot positions.csv")
        contents (slurp path)]
    (-> (csv/read-csv contents)
        (parse-spot-positions)
        (indexed-df->rect-df :missing-fill -1))))

(defn read-tm-tm-df []
  (let [path (io/resource "responses/Jak skutecznie miejsca (Responses) - TM_TM.csv")]
    (with-open [reader (io/reader path)]
      (-> (csv/read-csv reader)
          (parse-tm-tm-rows)
          (indexed-df->rect-df :missing-fill 2)))))

(defn read-tm-s-df []
  (let [path (io/resource "responses/Jak skutecznie miejsca (Responses) - TM_S.csv")]
    (with-open [reader (io/reader path)]
      (-> (csv/read-csv reader)
          (parse-tm-s-rows)
          (indexed-df->rect-df :missing-fill 2)))))

(defn read-responses-dfs []
  {:s-s (read-spot-positions-df)
   :tm-s (read-tm-s-df)
   :tm-tm (read-tm-tm-df)})

(comment
  (-> (read-spot-positions-df)
      (clojure.pprint/pprint))

  (transpose [[1 2] [3 4]])

  (-> (read-tm-tm-df)
      (clojure.pprint/pprint))

  (-> (read-tm-s-df)
      (clojure.pprint/pprint))

  (-> (read-responses-dfs)
      (clojure.pprint/pprint))

  )

