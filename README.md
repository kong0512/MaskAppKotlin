# MaskAppKotlin
[오준석님](https://github.com/junsuk50)의 인프런 강의 [모던 안드로이드 - 코틀린과 Jetpack 활용](https://www.inflearn.com/course/%EB%AA%A8%EB%8D%98-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%BD%94%ED%8B%80%EB%A6%B0-%EC%A0%9C%ED%8A%B8%ED%8C%A9)를 수강하면서 해당 강의에서 개발한 '공적 마스크 앱'을 따라 코딩한 것입니다. 본 문서에서는 해당 강의를 듣고, 새로 학습한 내용을 설명합니다.

## 주요 학습 내용
 - Room
 - MVVM 아키텍처(ViewModel/DataBinding/LiveData)
 - HTTP REST API 통신(Retrofit2)
 - Kotlin Coroutine
 - Dependency Injection(Dagger-Hilt)

### Room
[Android Developers에서의 소개 링크](https://developer.android.com/training/data-storage/room?hl=ko)

 - SQLite에 대한 추상화 레이어를 제공하여 빠르게 SQLite 데이터베이스에 액세스할 수 있도록 도와주는 라이브러리로, Jetpack에 포함
 - 특정 데이터 클래스를 생성하고 해당 데이터 클래스에 대한 DAO 인터페이스를 작성하면 해당 인터페이스를 호출하는 것으로 빠르게 SQLite DB에 접근할 수 있음
 - 개인적으로 써본 경험으로는, Spring Data JPA같은 느낌이 강했음.
 
#### 사용법
1. Gradle에 다음 dependency 추가 후 빌드
```
dependencies {
  def room_version = "2.2.5"

  implementation "androidx.room:room-runtime:$room_version"
  kapt "androidx.room:room-compiler:$room_version"

  // optional - Kotlin Extensions and Coroutines support for Room
  implementation "androidx.room:room-ktx:$room_version"

  // optional - Test helpers
  testImplementation "androidx.room:room-testing:$room_version"
}
```
2. 데이터 클래스로 이루어진 Entity 작성. 특정한 데이터 객체용 클래스를 만들고 여기에 @Entity를 달면 됨. 하나의 Entity=하나의 테이블이 됨.
```
  @Entity
  data class User(
        @PrimaryKey val uid: Int,
        @ColumnInfo(name = "first_name") val firstName: String?,
        @ColumnInfo(name = "last_name") val lastName: String?
  )
```
3. DAO 작성. 인터페이스를 하나 만든 뒤 이 안에 CRUD에 필요한 메소드를 만들고 해당 메소드에 @Query, @Insert, @Delete 등을 단 뒤, 인터페이스에 @Dao 어노테이션을 달면 됨. 개발자가 데이터를 DB에 입출력하려면 이 Dao 인터페이스를 사용.
```
 @Dao
    interface UserDao {
        @Query("SELECT * FROM user")
        fun getAll(): List<User>

        @Query("SELECT * FROM user WHERE uid IN (:userIds)")
        fun loadAllByIds(userIds: IntArray): List<User>

        @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
               "last_name LIKE :last LIMIT 1")
        fun findByName(first: String, last: String): User

        @Insert
        fun insertAll(vararg users: User)

        @Delete
        fun delete(user: User)
    }
```
4. AppDatabase 작성. 데이터베이스 홀더를 포함하는 기본적인 액세스 포인트로, 반드시 RoomDatabase를 상속하는 추상 클래스여야 하고, Entity가 반드시 포함되어 있어야 하며, 각각의 Dao들을 모두 추상 메소드로 지정해야 함.
```
 @Database(entities = arrayOf(User::class), version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun userDao(): UserDao
    }
```
5. 다음 코드로 데이터베이스 인스턴스를 가져옴.
```
  val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database-name"
            ).build()
```
6. 아래와 같은 코드식으로 데이터를 입출력하되, 가급적이면 비동기 처리를 하는 것을 권장
```
db.userDao().insertAll(user)
```

### MVVM 아키텍처
<img src="https://developer.android.com/topic/libraries/architecture/images/final-architecture.png"/>

 - Model-View-ViewModel 아키텍처의 준말로, Model(데이터 부분)과 View(UI 부분)을 ViewModel이라는 컴포넌트가 매개체 역할을 함.
 - ViewModel에서 대부분의 비즈니스 로직을 처리하며, View는 ViewModel과 데이터 바인딩으로 연결되어 ViewModel에서 특정 데이터가 변경되면 View로 바로 변경됨.

#### LiveData
#### ViewModel
#### DataBinding

### Coroutine

### Retrofit2

### Dependency Injection
