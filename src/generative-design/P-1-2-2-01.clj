(ns generative-design.P-1-2-2-01
  [:require [quil.core :as q]
   [quil.middleware :as m]])
(use 'infix.macros)
(refer 'infix.macros :only '[$=])


(def image1 "/Users/andriylevitskyy/Desktop/generative_art/generative-art-experiments/src/generative-design/data/P-1-2-2-01/data/pic1.jpg")
(def image2 "/Users/andriylevitskyy/Desktop/generative_art/generative-art-experiments/src/generative-design/data/P-1-2-2-01/data/pic2.jpg")
(def image3 "/Users/andriylevitskyy/Desktop/generative_art/generative-art-experiments/src/generative-design/data/P-1-2-2-01/data/pic3.jpg")
(def image4 "/Users/andriylevitskyy/Desktop/generative_art/generative-art-experiments/src/generative-design/data/P-1-2-2-01/data/pic4.jpg")
(def image (atom nil))
(def sorting-function (atom nil))

(defn setup []
  (q/frame-rate 10)
  (q/no-stroke)
  (q/color-mode :rgb 255)
  (reset! image (q/load-image image1)))

(defn draw-state [state]
  (let [tile-count (q/floor (/ (q/width) (max 5 (/ (q/mouse-x) 2))))
        rect-size (/ (q/width) tile-count)
        pixels (q/pixels @image)
        grid-x (range 0 tile-count)
        grid-y (range 0 tile-count)
        grid-xy (for [x grid-x y grid-y] [x y])
        grid-xx (map first grid-xy)
        grid-yy (map second grid-xy)
        is (for [x grid-x y grid-y] (+ (* (* y rect-size) (q/width)) (* x rect-size)))
        colors (map #(nth pixels %) is)
        params (if (nil? @sorting-function)
                 (map vector grid-xx grid-yy colors)
                 (map vector grid-xx grid-yy (sort-by @sorting-function colors)))]
    (doseq [[x y col] params]
      (q/fill (q/red col) (q/green col) (q/blue col))
      (q/rect (* x rect-size) (* y rect-size) rect-size rect-size))))

(defn key-pressed [state {:keys [key key-code]}]
  (case key
    :s (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_1_2_2_01.jpg")
    :S (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_1_2_2_01.jpg")
    :1 (reset! image (q/load-image image1))
    :2 (reset! image (q/load-image image2))
    :3 (reset! image (q/load-image image3))
    :4 (reset! image (q/load-image image4))
    :5 (reset! sorting-function nil)
    :6 (reset! sorting-function q/hue)
    :7 (reset! sorting-function q/brightness)
    :8 (reset! sorting-function q/saturation)
    :9 (reset! sorting-function identity)))

(q/defsketch sketch
  :title "test"
  :size [600 600]
  :setup setup
  :draw draw-state
  :middleware [m/fun-mode]
  :key-pressed key-pressed)



