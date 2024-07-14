(ns website.core
  (:require [gd.heart :as heart]
            [gd.P-1-0-01 :as P-1-0-01]
            [gd.P-1-2-3-05 :as P-1-2-3-05]))

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

(def sketch-registry [["Heart sketch" heart/run-sketch]
                      ["P_1_0_01" P-1-0-01/run-sketch]
                      ["P_1_2_3_05" P-1-2-3-05/run-sketch]])


(defn ^:export render []
  (render-list sketch-registry))
