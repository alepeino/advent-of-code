(ns day3.a
  (:require [clojure.java.io :as io])
  (:require [clojure.set :as set])
  (:require [clojure.string :as str]))

(defn- input [] (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(defn- split-half [s] (-> s count (/ 2) (split-at s)))

(defn- common-item [parts] (first (apply set/intersection (map set parts))))

(def priorities "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")

(defn- priority [c] (str/index-of priorities c))

(defn -main []
  (->> (str/split-lines (input))
    (map split-half)
    (map common-item)
    (map priority)
    (apply +)
    prn))

(-main)