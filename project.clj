(defproject cljtree-graalvm "0.1.0-SNAPSHOT"
  :description "Tree command in Clojure"
  :url "https://github.com/borkdude/cljtree-graalvm"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.7"]
                 [io.aviso/pretty "0.1.34"]]
  :main cljtree-graalvm.core
  :profiles {:uberjar {:jvm-opts ["-Dclojure.compiler.direct-linking=true"]
                       :aot :all}})
