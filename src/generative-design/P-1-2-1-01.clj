(ns generative-design.P-1-2-1-01
  (:require [quil.core :as q]
            [quil.middleware :as m]))

; DESCRIPTION: Color grid controled by mouse and keyboard.
; interpolate-shortest not working
; shake-colors does not work with tiles less than 10
; colors look  different from original sketch

(def width 800)
(def height 800)
(def colors-left (atom []))
(def colors-right (atom []))
(def lerp-func (atom q/lerp-color))

(defn tile-count-x [mouse-x] (int (q/map-range (q/constrain mouse-x 0 width) 0 width 2 100)))
(defn tile-count-y [mouse-y] (int (q/map-range (q/constrain mouse-y 0 height) 0 height 2 10)))
(defn color-left [] (q/color (q/random 0 60) (q/random 0 100) 100))
(defn color-right [] (q/color (q/random 160 190) 100 (q/random 0 100)))



(defn shake-colors [tile-y]
  (reset! colors-left (repeatedly tile-y color-left))
  (reset! colors-right (repeatedly tile-y color-right)))

(defn setup []
  (q/frame-rate 10)
  (q/color-mode :hsb 256 100 100)
  (q/no-stroke)
  (shake-colors 10))

(defn mouse-released [a b]
  ;(shake-colors (tile-count-y (q/mouse-y))))
  (shake-colors 10))

(defn lerpHSB [col1 col2 amount]
  (let [h1 (q/hue col1)
        s1 (q/saturation col1)
        b1 (q/brightness col1)
        h2 (q/hue col2)
        s2 (q/saturation col2)
        b2 (q/brightness col2)
        h (q/map-range amount 0 1 h1 h2)
        s (q/map-range amount 0 1 s1 s2)
        b (q/map-range amount 0 1 b1 b2)]
    ; Since we are already in hsb mode dont need to switch modes
    (q/color h s b)))

(defn draw-state [state]
  (let [tiles-x (tile-count-x (q/mouse-x))
        tiles-y (tile-count-y (q/mouse-y))
        tile-width (/ width tiles-x)
        tile-height (/ height tiles-y)
        grid-x (range 0 tiles-x)
        grid-y (range 0 tiles-y)
        grid-xy (for [x grid-x y grid-y] [x y])
        xs (map first grid-xy)
        ys (map second grid-xy)
        pos-x (map #(* tile-width %)  xs)
        pos-y (map #(* tile-height %) ys)
        col1 (map #(nth @colors-left %) ys)
        col2 (map #(nth @colors-right %) ys)
        amount (map #(q/map-range % 0 (- tiles-x 1) 0 1) xs)
        inter-col (map #(@lerp-func %1 %2 %3) col1 col2 amount)
        params (map vector pos-x pos-y inter-col)]
    (doseq [[x y col] params]
      (q/fill (q/hue col) (q/saturation col) (q/brightness col))
      (q/rect x y tile-width tile-height))))

(defn key-pressed [state {:keys [key key-code]}]
  (case key
    :s (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_1_2_1_01.jpg")
    :S (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_1_2_1_01.jpg")
    :1 (reset! lerp-func q/lerp-color)
    :2 (reset! lerp-func lerpHSB)))


(q/defsketch sketch
  :title "P-1-1-1-01"
  :size [width height]
  :setup setup
  :draw draw-state
  :middleware [m/fun-mode]
  :key-pressed key-pressed
  :mouse-released mouse-released)
