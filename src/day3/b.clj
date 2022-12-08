(ns day3.b
  (:require [clojure.java.io :as io])
  (:require [clojure.set :as set])
  (:require [clojure.string :as str]))

(defn- input [] (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(defn- common-item [parts] (first (apply set/intersection (map set parts))))

(def priorities "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")

(defn- priority [c] (str/index-of priorities c))

(defn -main []
  (->> (str/split-lines (input))
    (partition-all 3)
    (map common-item)
    (map priority)
    (apply +)
    prn))

(-main)