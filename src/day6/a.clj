(ns day6.a)

(defn- input []
  (slurp "src/day6/res/input.txt"))

(def marker-width 4)

(defn -main []
  (loop [input (input)
         index marker-width]
    (if (->> input (take marker-width) frequencies count (= marker-width))
      (prn index)
      (recur (next input) (inc index)))))

(-main)