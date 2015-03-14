# Input #

The parameters of the tool is supplied by URL, supported fields includes:

  * srcsent, tgtsent : source and target sentences, space separated string, UTF-8, URL encoded.
  * idxonsrc : "true" or "false", whether you want people to label source sentence or target sentence
  * idx : a zero-based sequence of integer, which words do you want users to label, underscore separated. e.g. 0\_1\_3\_5 means you want user to label the first, second, fourth and sixth word.
  * hnt : Initial alignment to show for users as a hint, the format is the same as the output.
  * assignmentId : used by mturk, automatically appended.

Sample URL:

[http://alt-aligner.appspot.com/?srcsent=%E8%BF%99%E4%BA%9B+%E8%A2%AB+%E4%BA%BA%E4%BB%AC+%E6%89%80+%E7%86%9F%E7%9F%A5+%E7%9A%84+%E5%8D%A1%E9%80%9A+%E5%BD%A2%E8%B1%A1+%E4%BB%A5+%E5%85%B6+%E7%8B%AC%E6%9C%89+%E7%9A%84+%E9%AD%85%E5%8A%9B+%E5%86%8D+%E4%B8%80+%E6%AC%A1+%E8%AE%A9+%E4%B8%96%E4%BA%BA+%E7%9A%84+%E7%9B%AE%E5%85%89+%E8%81%9A%E9%9B%86+%E5%88%B0+%E9%A6%99%E6%B8%AF+,&tgtsent=With+their+unique+charm+,+these+well+-+known+cartoon+images+once+again+caused+Hong+Kong+to+be+a+focus+of+worldwide+attention+.&idxonsrc=true&idx=0_1_2_3_4_5_6_7_8_9_10_11_12_13_14_15_17_16_19_18_21_20_23_22_&hint=1-1&assignmentId=ASSIGNMENT_ID_NOT_AVAILABLE&hitId=1KBVYL71878F2DT1VT6YTS1CSVL692

# Output #

It will post the result back to MTurk, the format is space separated links, such as

```
1-5 2-4 3-3 4-6 5-7 6-7 7-0 8-10 9-11 9-12 9-13 9-14 9-15 9-16 10-0
11-8
```
The first index is source, the second is target, and all 1-based, zero
means aligned to null-word.