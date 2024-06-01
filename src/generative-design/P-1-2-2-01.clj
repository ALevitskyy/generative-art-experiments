(ns generative-design.P-1-2-2-01
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def image1 "/Users/andriylevitskyy/Desktop/generative_art/generative-art-experiments/src/generative-design/data/P-1-2-2-01/data/pic1.jpg")
(def image2 "/Users/andriylevitskyy/Desktop/generative_art/generative-art-experiments/src/generative-design/data/P-1-2-2-01/data/pic2.jpg")
(def image3 "/Users/andriylevitskyy/Desktop/generative_art/generative-art-experiments/src/generative-design/data/P-1-2-2-01/data/pic3.jpg")
(def image4 "/Users/andriylevitskyy/Desktop/generative_art/generative-art-experiments/src/generative-design/data/P-1-2-2-01/data/pic4.jpg")
(def image (atom nil))
(def sortMode (atom nil))

(defn setup []
  (q/frame-rate 1)
  (q/no-cursor)
  (q/no-stroke)
  (reset! image (q/load-image image1)))

(defn max-seq [s]
  (apply max s))

(defn draw-state [state]
  (let [tile-count (q/floor (/ (q/width) (max 5 (q/mouse-x))))
        rect-size (/ (q/width) tile-count)
        pixels (q/pixels @image)
        grid-x (range 0 tile-count)
        grid-y (range 0 tile-count)
        grid-xy (for [x grid-x y grid-y] [x y])
        px (map #(int (* (first %) rect-size)) grid-xy)
        py (map #(int (* (second %) rect-size)) grid-xy)
        image-width (. @image width)
        i (for [x px y py] (* 4 (+ x (* y image-width))))
        ;_ (println (count pixels) (max-seq i))
        colors (map #(q/color (nth pixels %) (nth pixels (inc %)) (nth pixels (+ 2 %)) (nth pixels (+ 3 %))) i)
        sorted-colors colors
        params (map vector px py sorted-colors)]
    (doseq [[x y col] params]
      (q/fill col)
      (q/rect (* x rect-size) (* y rect-size) rect-size rect-size))))


(defn key-pressed [state {:keys [key key-code]}]
  (case key
    :s (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_1_2_2_01.jpg")
    :S (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_1_2_2_01.jpg")
    :1 (reset! image (q/load-image image1))
    :2 (reset! image (q/load-image image2))
    :3 (reset! image (q/load-image image3))
    :4 (reset! image (q/load-image image4))
    :5 (reset! sortMode nil)
    :6 (reset! sortMode :hue)
    :7 (reset! sortMode :saturation)
    :8 (reset! sortMode :brightness)
    :9 (reset! sortMode :grayscale)))

(q/defsketch sketch
  :title "P-1-1-1-01"
  :size [600 600]
  :setup setup
  :draw draw-state
  :middleware [m/fun-mode]
  :key-pressed key-pressed)