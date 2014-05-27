(ns captchabreak.core
  (:require [clj-http.client :as client]
            [clojure.java.io :as io]
            )
  )

;currently only gets the ugly version of the captcha....as getting javascript to work is a bit of ... work

(def some-http-request
  {:headers {
            "User-Agent" "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36"
            "Accept" "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
            "Accept-Language" "en-US,en;q=0.8"
            "Accept-Encoding" "gzip,deflate,sdch"
            "Cache-Control" "no-cache"
            "Connection" "keep-alive"
            ;"Content-Type" "application/x-www-form-urlencoded"
            ;"Content-Encoding" "gzip"
            }
   })

(defn- get-challenge
  "get challenge key from google site using api-key"
  [api-key http-header]
  (let [http-resp (client/get (str "http://www.google.com/recaptcha/api/challenge?k=" api-key) http-header)
        http-resp-body (http-resp :body)
        ]
    (second (re-find #"challenge : '([\w-_]+)\'" http-resp-body))
    )
  )

(defn- get-captcha-api-key
  "get api key from original site"
  [http-body-result]
  (second(re-find #"noscript\?k=(\w+)" http-body-result))
  )

(comment (defn- get-challenge-key [http-resp-body]
           (second (re-find #"id=\"recaptcha_challenge_field\" value=\"([\w-_]+)\"" http-resp-body))
           ))

(defn- get-image-with-challenge-key
  [challenge-key http-header]
  (let [image-http-resp (client/get (str "http://www.google.com/recaptcha/api/image?c=" challenge-key) (conj {:as :byte-array} http-header))]
    (image-http-resp :body)
    )
  )

(defn- write-file [image-file]
   (with-open [w (clojure.java.io/output-stream "test-file.jpg")]
     (.write w image-file)))

(defn- main-get-captcha-image
  "takes a url and returns the captcha image from that site"
  [url http-header]
  (let [http-resp (client/get url http-header)
        http-resp-body (http-resp :body)
        api-key (get-captcha-api-key http-resp-body)
        challenge-key (get-challenge api-key http-header)
        image-file-binary (get-image-with-challenge-key challenge-key http-header)
        ]
    (println image-file-binary)
    (write-file image-file-binary)
                                        ;(get-image-with-challenge-key challenge-key http-header)
    )
  )

(defn- test-main-get-captcha-image []
  (main-get-captcha-image "http://www.wmssamples.com/user_interaction/recaptcha.aspx" some-http-request)
  )
