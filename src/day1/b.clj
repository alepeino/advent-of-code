(ns day1.b
  (:require [clojure.string :as str]))

(defn- input [] (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(defn -main []
  (->> (str/split (input) #"\n\n")
    (map (fn [x]
           (->> x
             str/split-lines
             (map #(Integer/parseInt %))
             (apply +))))
    (sort #(compare %2 %1))
    (take 3)
    (apply +)
    prn))

(-main)