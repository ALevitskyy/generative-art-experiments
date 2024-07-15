(ns gd.P-1-2-3-01
  [:require [quil.core :as q]
   [quil.middleware :as m]])

(def tileCountX 50)
(def tileCountY 50)
(def hueValues (atom []))
(def saturationValues (atom []))
(def brightnessValues (atom []))

(defn random-vector-len-n
  ([val n] (take n (repeatedly #(q/random val))))
  ([min max n] (take n (repeatedly #(q/random min max)))))

(defn constant-vector-len-n [val n]
  (vec (repeat n val)))

(defn interleave-2-vectors [v1 v2]
  (mapcat vector v1 v2))

(defn setup []
  (q/frame-rate 10)
  (q/color-mode :hsb 360 100 100 100)
  (q/no-stroke)
  (reset! hueValues (random-vector-len-n 360 tileCountX))
  (reset! saturationValues (random-vector-len-n 100 tileCountX))
  (reset! brightnessValues (random-vector-len-n 100 tileCountX)))

(defn draw-state [state]
  (let [_ (q/background 0 0 100)
        mx (q/constrain (q/mouse-x) 0 (q/width))
        my (q/constrain (q/mouse-y) 0 (q/height))
        currentTileCountX (int (q/map-range mx 0 (q/width) 1 tileCountX))
        currentTileCountY (int (q/map-range my 0 (q/height) 1 tileCountY))
        tileWidth (/ (q/width) currentTileCountX)
        tileHeight (/ (q/height) currentTileCountY)
        grid-xy (for [x (range tileCountX) y (range tileCountY)] [x y])
        grid-xx (map first grid-xy)
        grid-yy (map second grid-xy)
        posX (map #(* % tileWidth) grid-xx)
        posY (map #(* % tileHeight) grid-yy)
        index (map #(mod % currentTileCountX) (range (count grid-xx)))
        hs (map #(nth @hueValues %) index)
        ss (map #(nth @saturationValues %) index)
        bs (map #(nth @brightnessValues %) index)
        params (map vector hs ss bs posX posY)]
    (doseq [[h s b x y] params]
      (q/fill h s b)
      (q/rect x y tileWidth tileHeight))))

(defn key-pressed [state {:keys [key key-code]}]
  (case key
    :s (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_1_2_3_01.jpg")
    :S (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_1_2_3_01.jpg")
    :1 (do (reset! hueValues (random-vector-len-n 360 tileCountX))
           (reset! saturationValues (random-vector-len-n 100 tileCountX))
           (reset! brightnessValues (random-vector-len-n 100 tileCountX)))
    :2 (do (reset! hueValues (random-vector-len-n 360 tileCountX))
           (reset! saturationValues (random-vector-len-n 100 tileCountX))
           (reset! brightnessValues (constant-vector-len-n 100 tileCountX)))
    :3 (do (reset! hueValues (random-vector-len-n 360 tileCountX))
           (reset! saturationValues (constant-vector-len-n 100 tileCountX))
           (reset! brightnessValues (random-vector-len-n 100 tileCountX)))
    :4 (do (reset! hueValues (constant-vector-len-n 0 tileCountX))
           (reset! saturationValues (constant-vector-len-n 0 tileCountX))
           (reset! brightnessValues (random-vector-len-n 100 tileCountX)))
    :5 (do (reset! hueValues (constant-vector-len-n 195 tileCountX))
           (reset! saturationValues (constant-vector-len-n 100 tileCountX))
           (reset! brightnessValues (random-vector-len-n 100 tileCountX)))
    :6 (do (reset! hueValues (constant-vector-len-n 195 tileCountX))
           (reset! saturationValues (random-vector-len-n 100 tileCountX))
           (reset! brightnessValues (constant-vector-len-n 100 tileCountX)))
    :7 (do (reset! hueValues (random-vector-len-n 180 tileCountX))
           (reset! saturationValues (random-vector-len-n 80 100 tileCountX))
           (reset! brightnessValues (random-vector-len-n 50 90 tileCountX)))
    :8 (do (reset! hueValues (random-vector-len-n 180 360 tileCountX))
           (reset! saturationValues (random-vector-len-n 80 100 tileCountX))
           (reset! brightnessValues (random-vector-len-n 50 90 tileCountX)))
    :9 (do (reset! hueValues (interleave-2-vectors
                              (random-vector-len-n 360 (/ tileCountX 2))
                              (constant-vector-len-n 195 (/ tileCountX 2))))
           (reset! saturationValues (interleave-2-vectors
                                     (constant-vector-len-n 100 (/ tileCountX 2))
                                     (random-vector-len-n 100 (/ tileCountX 2))))
           (reset! brightnessValues (interleave-2-vectors
                                     (random-vector-len-n 100 (/ tileCountX 2))
                                     (constant-vector-len-n 100 (/ tileCountX 2)))))
    :0 (do (reset! hueValues (interleave-2-vectors
                              (constant-vector-len-n 140 (/ tileCountX 2))
                              (constant-vector-len-n 215 (/ tileCountX 2))))
           (reset! saturationValues (interleave-2-vectors
                                     (random-vector-len-n 30 100 (/ tileCountX 2))
                                     (random-vector-len-n 40 100 (/ tileCountX 2))))
           (reset! brightnessValues (interleave-2-vectors
                                     (random-vector-len-n 40 100 (/ tileCountX 2))
                                     (random-vector-len-n 50 100 (/ tileCountX 2)))))))

(def sketch (atom nil))

(defn run-sketch []
  (q/defsketch sketch
    :title "test"
    :size [1000 1000]
    :setup setup
    :draw draw-state
    :middleware [m/fun-mode]
    :key-pressed key-pressed))

