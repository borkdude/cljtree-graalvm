(ns cljtree-graalvm.core
  "Tree command, inspired by https://github.com/lambdaisland/birch"
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :refer [pprint]]
            [clojure.tools.cli :refer [parse-opts]])
  (:import [java.io.File])
  (:gen-class))

(def I-branch "│   ")

(def T-branch "├── ")

(def L-branch "└── ")

(def SPACER   "    ")

(defn file-tree
  [^java.io.File path]
  (let [children (.listFiles path)
        dir? (.isDirectory path)]
    (cond->
     {:name (.getName path)
      :type (if dir? "directory" "file")}
      dir? (assoc :contents
                  (map file-tree children)))))

(defn render-tree
  [file-tree]
  (let [children (:contents file-tree)]
    (cons (:name file-tree)
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

(defn stats
  [file-tree]
  (apply merge-with +
         {:total 1
          :directories (case (:type file-tree)
                         "directory" 1
                         0)}
         (map stats (:contents file-tree))))

(def cli-options [["-e" "--edn" "Output tree as EDN"]])

(defn -main [& args]
  (let [{:keys [options arguments]}
        (parse-opts args cli-options)
        path (io/file
              (or (first arguments)
                  "."))
        tree (file-tree path)
        {:keys [total directories]}
        (stats tree)]
    (if (:edn options)
      (pprint tree)
      (do
        (doseq [l (render-tree tree)]
          (println l))
        (println)
        (println
         (format "%s directories, %s files"
                 directories (- total directories)))))))

;;;; Scratch

(comment
  (-main "src" "-e"))
