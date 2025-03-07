#lang racket/base
(display "Hello, World!\n")

;;; CMPSC 461 PRACTICE SHEET 3

; easiest
#;(define (intersperse sep lst)
  (cond 
    [(null? lst) '()]
    [(null? (cdr lst)) lst]
    [else 
      (append 
        (list (car lst) sep) 
        (intersperse sep (cdr lst))
      )
    ]
  )
)

; higher-order
; use foldr
(define (intersperse sep lst)
  (cond 
    [(null? lst) '()]
    [(null? (cdr lst)) lst]
    [else 
      (append 
        (list (car lst) sep) 
        (intersperse sep (cdr lst))
      )
    ]
  )
)

(display "\nintersperse\n")
(intersperse 0 '(1 2 3 4)) ; Should return '(1 0 2 0 3 0 4)
(intersperse "and" '("a" "b")) ; Should return '("a" "and" "b")
(intersperse "," '()) ; Should return '()
(intersperse "," '(1)) ; Should return '(1)

; easiest
#;(define (filter-evens lst)
  (cond 
    [(null? lst) '()]
    [(even? (car lst)) 
      (cons 
        (car lst)
        (filter-evens (cdr lst))
      )
    ]
    [else (filter-evens (cdr lst))]
  )
)

; higher-order
(define (filter-evens lst)
  (filter even? lst)
)

(display "\nfilter-evens\n")
(filter-evens '(1 2 3 4 5 6)) ; Returns '(2 4 6)

; easiest
#;(define (reverse-list lst)
  (if (null? lst)
    '()
    (append (reverse-list (cdr lst)) (list (car lst)))
  )
)

; higher-order
(define (reverse-list lst) 
  (foldl cons '() lst)
)

(display "\nreverse-list\n")
(reverse-list '(1 2 3 4)) ; Should return '(4 3 2 1)

#|
pseudo code
vector flatten(lst)
  acc = '()
  for (elem: lst)
    val = elem
    if (elem is vector)
      val = flatten(elem)
    acc += val
|#

; easiest
#;(define (flatten lst)
  (cond
    [(null? lst) '()]
    [(list? (car lst)) (append (flatten (car lst)) (flatten (cdr lst)))]
    [else (cons (car lst) (flatten (cdr lst)))]
  )
)

; higher-order
(define (flatten lst)
  (foldr
    (lambda (x acc)
      (append
        (if (list? x) (flatten x) (list x))
        acc
      )
    )
    '() 
    lst
  )
)

(display "\nflatten\n")
(flatten '(1 (2 (3 4) 5) 6)) ; Returns '(1 2 3 4 5 6)
(flatten '(1 2 ((2 4) 3) 3 (((3) 2) 1) 0)) ; Returns '(1 2 2 4 3 3 3 2 1 0)

; higher-order
(define (qs lst)
  (if (null? lst)
    lst
    (append 
      (qs (filter (lambda (elem) (<= elem (car lst))) (cdr lst)))
      (list (car lst))
      (qs (filter (lambda (elem) (> elem (car lst))) (cdr lst)))
    )
  )
)

(display "\nqs\n")
(qs '(3 6 8 10 1 2 1)) ; Should return '(1 1 2 3 6 8 10)