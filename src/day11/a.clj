(ns day11.a
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str]))

(defn- input []
  (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(def monkey-regex #"(?m)Monkey (\d+):\n\s+Starting items: ((?:\d*(?:, |$))*)\n\s+Operation: new = (.+$)\n\s+Test: divisible by (\d+)$\n\s+If true: throw to monkey (\d+)$\n\s+If false: throw to monkey (\d+)$")

(defrecord Monkey [id items operation test iftrue iffalse activity])

(defn- operation-to-fn [s]
  (let [[arg1 operator arg2] (str/split s #" ")]
    (fn [old]
      (let [get-arg #(case % "old" old (Integer/parseInt %))]
        ((eval (symbol operator)) (get-arg arg1) (get-arg arg2))))))

(defn- parse-monkey [line]
  (let [[_ id items operation test iftrue iffalse] (re-find monkey-regex line)
        items     (map #(Integer/parseInt %) (str/split items #", "))
        operation (operation-to-fn operation)
        test      (Integer/parseInt test)]
    (Monkey. id items operation test iftrue iffalse 0)))

(defn- key-by [pred coll] (->> coll (map (juxt pred identity)) (into {})))

(defn- run-turn [state monkey-id]
  (let [{:keys [items operation test iftrue iffalse]} (state monkey-id)]
    (reduce
      (fn [new-state item]
        (let [new-item      (-> item operation (/ 3) Math/floor long)
              target-monkey (case (mod new-item test) 0 iftrue iffalse)]
          (-> new-state
            (update-in [monkey-id :items] rest)
            (update-in [target-monkey :items] concat (list new-item)))))
      (update-in state [monkey-id :activity] + (count items))
      items)))

(defn- run-rounds [total-rounds state]
  (let [monkey-ids (-> state keys sort)]
    (loop [round 0
           state state]
      (if (= total-rounds round)
        state
        (recur (inc round) (reduce run-turn state monkey-ids))))))

(defn -main []
  (->> (str/split (input) #"\n\n")
    (map parse-monkey)
    (key-by :id)
    (run-rounds 20)
    vals
    (map :activity)
    sort
    reverse
    (take 2)
    (apply *)
    prn))

(-main)