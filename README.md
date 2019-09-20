# StringObjectHashMap
StringObjectHashMap 继承 HashMap<String, Object>，提供了 getInteger、 getString 等方法。
## Usage
```java
StringObjectHashMap hashMap = new StringObjectHashMap();
hashMap.put("name","aa");
hashMap.put("age",12);
System.out.println(hashMap.getString("name"));
System.out.println(hashMap.getInteger("age"));
```
