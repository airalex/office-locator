(ns locator.dev
  (:require [nrepl.server :as nrepl]))

(defn nrepl-handler []
  (require 'cider.nrepl)

  ;; workaround for https://github.com/clojure-emacs/cider-nrepl/issues/447
  (ns-resolve 'cider.nrepl 'cider-nrepl-handler))

(defn start-nrepl []
  (let [port 7888]
    (nrepl/start-server :port port :handler (nrepl-handler))
    (spit ".nrepl-port" port)
    (println "nREPL started on port" port)))

(defn -main []
  (start-nrepl))

