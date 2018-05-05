(ns cljtree-graalvm.core
  "Tree command, inspired by https://github.com/lambdaisland/birch"
  (:require [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]
            [clojure.string :as str]
            [clojure.tools.cli :refer [parse-opts]]
            [io.aviso.ansi :as ansi])
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
  [{:keys [:name :contents]} colorize?]
  (cons (if colorize?
          (ansi/blue name)
          name)
        (mapcat
         (fn [child index]
           (let [subtree (render-tree child colorize?)
                 last? (= index (dec (count contents)))
                 prefix-first (if last? L-branch T-branch)
                 prefix-rest  (if last? SPACER I-branch)]
             (cons (str prefix-first (first subtree))
                   (map #(str prefix-rest %) (next subtree)))))
         contents
         (range))))

(defn stats
  [file-tree]
  (apply merge-with +
         {:total 1
          :directories (case (:type file-tree)
                         "directory" 1
                         0)}
         (map stats (:contents file-tree))))

(def cli-options [["-E" "--edn" "Output tree as EDN"]
                  ["-c" "--color" "Colorize the output"]])

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
        (doseq [l (render-tree tree (:color options))]
          (println l))
        (println)
        (println
         (format "%s directories, %s files"
                 directories (- total directories)))))))

;;;; Scratch

(comment
  (-main "src" "-e" "-c"))
