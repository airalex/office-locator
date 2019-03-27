(ns locator.sheet
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.set :as set]))


(defn csv-rows->maps [rows]
  (map zipmap
       (->> (first rows)
            (repeat))
       (rest rows)))

(defn parse-tm-tm-rows [rows]
  (let [colleagues (-> (first rows)
                       (rest))
        respondents (-> (map first rows)
                        (rest))
        _ (assert (set/subset? (set respondents)
                               (set colleagues)))
        cells (->> (rest rows)
                   (map (fn [[respondent & scores]]
                          (map-indexed (fn [score-i score]
                                         (let [colleague (nth colleagues score-i)]
                                           [{:row respondent
                                             :col colleague}
                                            (Integer/parseInt score)]))
                                       scores)))
                   (apply concat)
                   (into {}))
        all-labels (-> (set/union (set colleagues) (set respondents))
                       sort
                       vec)]
    {:indexed-cells cells
     :col-names all-labels
     :row-names all-labels}))

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

(comment
  (println "ab")

  (-> (csv/read-csv "a,b,c\n1,2,3\n4,5,6")
      (csv-rows->maps))

  (transpose [[1 2] [3 4]])

  (-> (let [path (io/resource "responses/Jak skutecznie miejsca (Responses) - TM_TM.csv")]
        (with-open [reader (io/reader path)]
          (-> (csv/read-csv reader)
              (parse-tm-tm-rows)
              (indexed-df->rect-df :missing-fill 0))))
      (clojure.pprint/pprint))

  )

