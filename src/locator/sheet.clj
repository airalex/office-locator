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

