(ns cljtree-graalvm.core
  "Tree command, inspired by https://github.com/lambdaisland/birch"
  (:require [clojure.string :as str]
            [clojure.java.io :as io])
  (:import [java.io.File])
  (:gen-class))

(def I-branch "│   ")

(def T-branch "├── ")

(def L-branch "└── ")

(def SPACER   "    ")

(defn prefix-line
  [entry prefix]
  (update entry :line #(str prefix %)))

(defn render-tree [^java.io.File path]
  (let [children (.listFiles path)]
    (cons {:line (.getName path)
           :dir? (.isDirectory path)}
          (mapcat
           (fn [child index]
             (let [subtree (render-tree child)
                   last? (= index (dec (count children)))
                   prefix-first (if last? L-branch T-branch)
                   prefix-rest  (if last? SPACER I-branch)]
               (cons (prefix-line (first subtree) prefix-first)
                     (map #(prefix-line % prefix-rest) (next subtree)))))
           children
           (range)))))

(defn -main [& args]
  (let [path (io/file
              (or (first args)
                  "."))
        entries (render-tree path)
        {:keys [total dirs]}
        (reduce (fn [acc {:keys [line dir?]}]
                  (println line)
                  (-> acc
                      (update :total inc)
                      (cond-> dir?
                        (update :dirs inc))))
                {:total 0
                 :dirs 0}
                entries)]
    (println)
    (println
     (format "%s directories, %s files"
             dirs (- total dirs)))))

;;;; Scratch

(comment
  (-main "/tmp/cljtree-graalvm"))
