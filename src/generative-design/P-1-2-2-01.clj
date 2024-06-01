(ns generative-design.P-1-2-2-01
  [:require [quil.core :as q]
   [quil.middleware :as m]])


(def image1 "/Users/andriylevitskyy/Desktop/generative_art/generative-art-experiments/src/generative-design/data/P-1-2-2-01/data/pic1.jpg")
(def image (atom nil))

(defn setup []
  (q/frame-rate 15)
  (q/no-stroke)
  (q/color-mode :rgb 255)
  (reset! image (q/load-image image1)))

(defn draw-state [state]
  (let [tile-count (q/floor (/ (q/width) (max 5 (q/mouse-x))))
        rect-size (/ (q/width) tile-count)
        _ (println "rect-size" rect-size "tile-count" tile-count)
        pixels (q/pixels @image)
        grid-x (range 0 tile-count)
        grid-y (range 0 tile-count)
        grid-xy (for [x grid-x y grid-y] [x y])
        grid-xx (map first grid-xy)
        grid-yy (map second grid-xy)
        is (for [x grid-x y grid-y] (+ (* (* y rect-size) (q/width)) (* x rect-size)))
        colors (map #(nth pixels %) is)
        params (map vector grid-xx grid-yy colors)]
    (doseq [[x y col] params] (q/fill (q/red col) (q/green col) (q/blue col))
           (q/rect (* x rect-size) (* y rect-size) rect-size rect-size))))

(q/defsketch sketch
  :title "test"
  :size [600 600]
  :setup setup
  :draw draw-state
  :middleware [m/fun-mode])