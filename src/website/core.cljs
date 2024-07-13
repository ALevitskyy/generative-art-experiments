(ns website.core
  (:require [gd.heart :as heart]))

(defn render-sketch! [text render-fn]
  (let [ul (. js/document getElementById "sketch-list")
        li (. js/document createElement "li")]
    (set! (. li -innerHTML) text)
    (set! (. li -style) "cursor:pointer")
    (.addEventListener li "click" render-fn)
    (.appendChild ul li)))

(defn render-list [links]
  ; Will redner all the links
  (doseq [[text render-fn] links]
    (render-sketch! text render-fn)))

(def sketch-registry [["Heart sketch" heart/run-sketch]])


(defn ^:export render []
  (render-list sketch-registry))
