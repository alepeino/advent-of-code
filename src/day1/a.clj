(ns day1.a
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str]))

(defn- input [] (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(defn -main []
  (->> (str/split (input) #"\n\n")
    (map (fn [x]
           (->> x
             str/split-lines
             (map #(Integer/parseInt %))
             (apply +))))
    (apply max)
    prn))

(-main)