(ns day12.a
  (:require [clojure.data.priority-map :refer [priority-map]])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str]))

(defn- input []
  (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(defn- parse-input [s]
  (let [grid       (->> (str/split-lines s) (mapv vec))
        get-coords #(let [width (count (grid 0))
                          pos   (str/index-of s %)
                          row   (int (/ pos width))]
                      [row (- (mod pos width) row)])]
    [grid (get-coords "S") (get-coords "E")]))

(defn- get-height [ch]
  (case ch \S (int \a) \E (int \z) (int ch)))

(defn- can-move [grid from to]
  (let [from-height (get-height (get-in grid from))
        to-height   (get-height (get-in grid to))]
    (<= (- to-height from-height) 1)))

(defn- neighbors [grid node]
  (let [rows (dec (count grid))
        cols (dec (count (grid 0)))]
    (for [move [[0 1] [1 0] [0 -1] [-1 0]]
          :let [next-node (mapv + node move)]
          :when (and (<= 0 (next-node 0) rows) (<= 0 (next-node 1) cols))
          :when (can-move grid node next-node)]
      next-node)))

(defn- map-vals [m f]
  (into {} (for [[k v] m] [k (f v)])))

(defn- remove-keys [m pred]
  (select-keys m (filter (complement pred) (keys m))))

(defn- dijkstra
  "Computes single-source shortest path distances in a directed graph.
   Given a node n, (f n) should return a map with the successors of n as keys
   and their (non-negative) distance from n as vals.
   Returns a map from nodes to their distance from start.

   https://gist.github.com/ummels/86c09182dee25b142280
   "
  [start f]
  (loop [q (priority-map start 0) r {}]
    (if-let [[v d] (peek q)]
      (let [dist (-> (f v) (remove-keys r) (map-vals (partial + d)))]
        (recur (merge-with min (pop q) dist) (assoc r v d)))
      r)))

(defn- shortest-path [grid start finish]
  (let [successors #(into {} (for [n (neighbors grid %)] [n 1]))
        distances  (dijkstra start successors)]
    (distances finish)))

(defn -main []
  (->> (input)
    parse-input
    (apply shortest-path)
    prn))

(-main)