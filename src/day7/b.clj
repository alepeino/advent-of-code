(ns day7.b
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [clojure.core.match :refer [match]]))

(defn- input [] (slurp (io/resource (str/replace *ns* #"\..$" "/input.txt"))))

(defn- split-commands [s] (-> s (subs 2) (str/split #"\n\$ ")))
(defn- parse-command [s] (-> s (str/replace #"(^\w+)\n" "$1 \n") (str/split #"[\s\n]" 3)))

(defprotocol FileSystemEntry
  (get-size [_])
  (is-dir [_]))

(defrecord File [name size]
  FileSystemEntry
  (get-size [this] (:size this))
  (is-dir [_] false))

(defrecord Directory [name fs]
  FileSystemEntry
  (get-size [this] (apply + (map get-size ((comp vals :fs) this))))
  (is-dir [_] true))

(defn- entry-from-string [s]
  (match (str/split s #" ")
    ["dir" name] (Directory. name {})
    [size-str name] (File. name (Integer/parseInt size-str))))

(defmulti run-command (fn [_state [cmd]] cmd))

(defmethod run-command "cd"
  [state [_cmd args]]
  (if (= ".." args)
    (update state :cwd #(subvec % 0 (dec (count %))))
    (update state :cwd #(conj % args))))

(defmethod run-command "ls"
  [state [_cmd _args output]]
  (let [path   (state :cwd)
        output (str/split-lines output)]
    (assoc-in state
      (interleave (repeat :fs) path)
      (Directory. (last path)
        (->> output (map entry-from-string) (map (juxt :name identity)) (into {}))))))

(defn- find-dirs [dir]
  (let [subdirs (->> dir :fs vals (filter is-dir))]
    (apply concat subdirs (map find-dirs subdirs))))

(defn- smallest-size-to-delete [[used & rest]]
  (let [total 70000000
        free (- total used)
        required 30000000
        to-delete (- required free)]
    (->> rest
      sort
      (drop-while (partial > to-delete))
      first)))

(defn -main []
  (->> (input)
    split-commands
    (map parse-command)
    (reduce run-command {:cwd [] :fs {}})
    find-dirs
    (map get-size)
    smallest-size-to-delete
    prn))

(-main)