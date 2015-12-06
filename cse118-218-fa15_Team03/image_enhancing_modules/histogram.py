import numpy as np
import cv2

img = cv2.imread('orig.jpg',0)
equ = cv2.equalizeHist(img)
#res = np.hstack((img,equ)) #stacking images side-by-side
#cv2.imwrite('res_compare.png',res)
cv2.imwrite('histogram_result.jpg',equ)