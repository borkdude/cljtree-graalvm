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

(defn render-tree [^java.io.File path]
  (let [children (.listFiles path)]
    (conj (mapcat
           (fn [child index]
             (let [subtree (render-tree child)
                   last? (= index (dec (count children)))
                   prefix-first (if last? L-branch T-branch)
                   prefix-rest  (if last? SPACER I-branch)]
               (cons (str prefix-first (first subtree))
                     (map #(str prefix-rest %) (next subtree)))))
           children
           (range))
          (.getName path))))

(defn -main [& args]
  (let [path (io/file
              (or (first args)
                  "."))
        tree (render-tree path)]
    (doseq [l tree]
      (println l))))

;;;; Scratch

(comment
  (-main "/tmp/cljtree-graalvm"))
