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
    (cons (.getName path)
          (mapcat
           (fn [child index]
             (let [subtree (render-tree child)
                   last? (= index (dec (count children)))
                   prefix-first (if last? L-branch T-branch)
                   prefix-rest  (if last? SPACER I-branch)]
               (cons (str prefix-first (first subtree))
                     (map #(str prefix-rest %) (next subtree)))))
           children
           (range)))))

(defn -main [& args]
  (->> (render-tree (io/file
                     (or (first args)
                         ".")))
       (str/join "\n")
       println))

;;;; Scratch

(comment
  (-main "/tmp/cljtree-graalvm"))
