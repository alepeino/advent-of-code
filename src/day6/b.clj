(ns day6.b
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str]))

(defn- input [] (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(def marker-width 14)

(defn -main []
  (loop [input (input)
         index marker-width]
    (if (->> input (take marker-width) frequencies count (= marker-width))
      (prn index)
      (recur (next input) (inc index)))))

(-main)