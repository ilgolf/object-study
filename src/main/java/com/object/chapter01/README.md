# 티켓판매 프로그램 개발

초대를 위한 초대 클래스 Invitation

```java
public class Invitation {
    private LocalDateTime when;
}
```

Ticket 정보를 담고 있는 클래스 Ticket

```java
public class Ticket {
    private Long fee;

    public Long getFee() {
        return fee;
    }
}
```

관람객 소지품을 보관하는 클래스 Bag

```java
public class Bag {
    private Long amount;
    private Invitation invitation;
    private Ticket ticket;
    
    // 초대장이 없고 현금만 보관할 수도 있다.
    public Bag(long amount) {
        this(null, amount);
    }
    
    public Bag(Invitation invitation, long amount) {
        this.invitation = invitation;
        this.amount = amount;
    }

    public boolean hasInvitation() {
        return invitation != null;
    }

    public boolean hasTicket() {
        return ticket != null;
    }

    public void addTicket(final Ticket ticket) {
        this.ticket = ticket;
    }
    
    public void minusAmount(final Long amount) {
        this.amount -= amount;
    }
    
    public void plusAmount(final Long amount) {
        this.amount += amount;
    }
}
```

관람객 정보를 들고 있는 클래스 Audience

```java
public class Audience {
    private Bag bag;

    public Audience(Bag bag) {
        this.bag = bag;
    }

    public Bag getBag() {
        return bag;
    }
}
```

매표소를 구현한 클래스 TicketOffice

```java
public class TicketOffice {
    private Long amount;
    private List<Ticket> tickets = new ArrayList<>();

    public TicketOffice(Long amount, Ticket... tickets) {
        this.amount = amount;
        this.tickets.addAll(Arrays.asList(tickets));
    }
    
    public Ticket getTicket() {
        return tickets.remove(0);
    }
    
    public void minusAmount(final Long amount) {
        this.amount -= amount;
    }
    
    public void plusAmount(final Long amount) {
        this.amount += amount;
    }
}
```

판매원을 구현한 클래스 Ticket Seller

```java
public class TicketSeller {
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public TicketOffice getTicketOffice() {
        return ticketOffice;
    }
}
```

각각의 애플리케이션의 핵심 클래스들을 구현했다. 이제 이를 실행할 Theater 클래스를 구현해보자 

```java
public class Theater {
    private TicketSeller ticketSeller;
    
    public Theater(TicketSeller ticketSeller) {
        this.ticketSeller = ticketSeller;
    }

    public void enter(Audience audience) {
        if (audience.getBag().hasInvitation()) {
            Ticket ticket = ticketSeller.getTicketOffice().getTicket();
            audience.getBag().addTicket(ticket);
        } else {
            Ticket ticket = ticketSeller.getTicketOffice().getTicket();
            audience.getBag().minusAmount(ticket.getFee());
            ticketSeller.getTicketOffice().plusAmount(ticket.getFee());
            audience.getBag().addTicket(ticket);
        }
    }
}
```

프로그램을 간단하게 설명하자면 Theater는 관람객의 초대장 여부를 검사하고 초대장이 들어있다면
이벤트에 당첨된 고객이므로 판매원에게 받은 티켓을 바로 가방안에 넣어준다. 가방 안에 초대장이 없다면 티켓을 판매하고 관람객의 가방에서 티켓 만큼 가진 금액을 차감
시키고 판매 했음으로 매표소에 금액을 증가시킨다. 마지막으로 가방안에 티켓을 넣어줌으로써 입장 절차가 끝난다. 

또한 테스트 코드도 잘 동작한다. (github 내 코드  참고)

하지만 이 코드는 몇가지 문제점이 있다.

# 무엇이 문제인가?

로버스 마틴은 클린 소프트웨어에서 모듈이 가져야하는 세 가지 기능에 대해 설명한다.

> 첫 번째는 실행 중에 제대로 동작해야한다.
> 
> 두 번째는 변경을 위해 존재해야 한다. 대부분의 모듈은 생명주기 동안 변경되기 때문에 간단한 작업만으로 변경이 가능해야한다. 변경이 어려운 모듈은 동작하더라도
> 개선해야한다.
> 
> 세 번째 특별한 훈련 없이도 개발자가 쉽게 읽고 이해할 수 있어야 한다. 읽는 사람과 의사 소통이 안된다면 모듈을 개선해야한다.

### 예상을 빗나가는 코드

현재 Theater가 가방을 직접 열어 초대장이 있는지 살펴보고 가방안에 초대장이 있으면 판매원이 매표소에 있는 티켓을 직접 꺼내와 가방안으로 옮긴다. 티켓을 구매할 때도
마찬가지다. 

이는 큰 문제가 있다. 각 객체가 독립적이지 못하다. 즉, Theater가 모든 객체의 동작을 통제한다. 또한 동작이 우리의 예상에서 크게 벗어난다. 
현실에선 관람객이 직접 자신의 가방에서 초대장을 직접 꺼내어 건내고 돈도 관람객이 가방에서 직접 꺼내어 판매원에게 지불한다. 판매원은 매표소에 있는 티켓을 직접
꺼내 관람객에게 건낸다. 하지만 코드안에선 그렇게 동작하지 않고 있다. 현재 코드는 상식과 너무 다르기 때문에 코드를 읽는 사람과 제대로 의사소통이 힘들다.

또한, 코드를 이해하려면 많은 정보드를을 한꺼번에 기억하고 있어야 한다. 필자는 테스트 코드를 짜면서 각각의 객체를 이해하기 위하여 어떤 객체가 어떤 객체를 들고 있는지를
자꾸 확인해야만 했다. 

### 변경에 취약한 코드

만약 관람객이 현금이 아니라 신용카드로 지불한다면 객체는 거의 하나로 연결 되어있다 싶이 되어있기 때문에 하나의 요구사항으로 인해 모든 코드가 흔들린다. 관람객이 가방
을 들고 있다는 가정이 바뀌면 Audience 클래스에서 Bag을 제거하는 것 뿐만 아니라 Audience의 Bag에 접근하는 Theater 클래스도 변경해주어야 한다. 

이것은 객체사이의 Dependency가 강력해서 문제가 생긴 것이다. Dependency를 완전히 없애라는 것이 아니다. 불필요한 의존성을 제거해서 최소한의 의존성만을 유지하
자는 것이다.

객체 사이의 의존성이 너무 과한 경우 결합도가 높다고 하는데 이 결합도를 낮추는 것을 decoupling이라고 한다. 설계 목표는 decoupling을 통해 변경에 용이하게 
만드는 것이다.

### 설계 개선하기

코드를 이해하기 어려운 이유는 Theater가 객체들에게 직접적으로 접근해서 모든 행동을 제어하는 것이다. 각각의 행동을 객체가 스스로 제어해야하는데
이러한 우리의 직관을 벗어났다.
또한 Theater가 TicketSeller와 Audience에 직접 접근하기 때문에 세 개는 변경이 일어나면 항상 같이 변경해주어야 한다.

### 자율성을 높이자

설계를 변경하기 힘든 이유는 Theater가 Audience와 TicketSeller뿐만 아니라 Audience 소유의 Bag과 TicketSeller가 근무하는 TicketOffice에도
접근하기 때문인데 그럼 자율적인 존재가 되도록 설계를 변경해서 해결해보자

첫 번째 단계는 Theater의 enter 메서드에서 TicketOffice에 접근하는 모든 코드를 TicketSeller내부로 숨기는 것이다. 

```java
public class TicketSeller {
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public void sellTo(Audience audience) {
        if (audience.getBag().hasInvitation()) {
            Ticket ticket = this.ticketOffice.getTicket();
            audience.getBag().addTicket(ticket);
        } else {
            Ticket ticket = this.ticketOffice.getTicket();
            audience.getBag().minusAmount(ticket.getFee());
            this.ticketOffice.plusAmount(ticket.getFee());
            audience.getBag().addTicket(ticket);
        }
    }
}
```

```java
public class Theater {
    private TicketSeller ticketSeller;

    public Theater(TicketSeller ticketSeller) {
        this.ticketSeller = ticketSeller;
    }

    public void enter(Audience audience) {
        ticketSeller.sellTo(audience);
    }
}
```

getTicketOffice 메서드가 삭제되어 TicketSeller외엔 ticketOffice에 접근이 불가능하다. 이제 TicketOffice를 통해 티켓을 꺼내거나 판매 요금을 적립
하는 건 TicketSeller만 할 수 있다. 

이처럼 개념적이나 물리적으로 객체 내부의 세부적인 사항을 감추는 것을 **캡슐화**라고 부른다. 캡슐화의 목적은 변경하기 쉬운 객체를 만드는 것이다. 캡슐화를 통해
객체 내부로의 접근을 제한한다면 객체와 객체 사이의 결합도를 낮출 수 있기 때문에 설계를 좀 더 쉽게 변경할 수 있게된다. 또한, Theater는 코드가 매우 간결해진다. 
seller의 sellTo 메시지를 이해하고 응답할 수 있기만 할 뿐 책임도 줄게 되었다.

Theater는 오직 TicketSeller interface에만 의존하고 내부에 TicketOffice 인스턴스를 포함하고 있다는 사실은 implementation의 영역에 속한다.
객체를 인터페이스와 구현으로 나누고 인터페이스만을 공개하는 것은 객체 사이의 결합도를 낮추고 변경하기 쉬운 코드를 작성하기 위해 따라야 하는 가장 기본적인 설계 원칙
이다. 

다음으로 Audience 클래스를 개선해보자 TicketSeller가 아직까지 Bag에 접근하고 있기 때문에 캡슐화를 통해 개선해줄 필요가 있다.

```java
public class Audience {
    private Bag bag;

    public Audience(Bag bag) {
        this.bag = bag;
    }

    public boolean hasTicket() {
        return this.bag.hasTicket();
    }

    public Long buy(final Ticket ticket) {
        if (this.bag.hasInvitation()) {
            this.bag.addTicket(ticket);
            return 0L;
        } else {
            this.bag.minusAmount(ticket.getFee());
            this.bag.plusAmount(ticket.getFee());
            this.bag.addTicket(ticket);
            return ticket.getFee();
        }
    }
}
```

```java
public class TicketSeller {
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public void sellTo(Audience audience) {
        Ticket ticket = this.ticketOffice.getTicket();
        ticketOffice.plusAmount(audience.buy(ticket));
    }
}
```

Audience가 자신의 가방 안에서 초대장이 있는지 없는지 유무를 스스로 확인한다. Bag은 외부에서 접근하고 있지 않기 때문에 외부의 제 3자가 열어볼 수 없다. 
캡슐화가 되었기 때문에 getBag 메서드를 제거하고 TicketSeller가 buy메서드를 호출하기만 하면 된다.

이제 Audience와 TicketSeller가 변경되더라도 Theater에는 영향을 미치지 않는다. 즉, 내부 문제는 내부에서 처리한다는 것이다. 자율적이게 된 것이다.

### 어떻게 한 것인가?

간단하다. 판매자가 티켓을 판매가하기 위해 TicketOffice를 사용하는 모든 부분을 TicketSeller 내부로 캡슐화하고 Audience가 티켓을 구매하기 위해 Bag을
사용하는 모든 부분을 Audience 내부로 캡슐화 하였다. 즉, 자신의 문제를 스스로 해결하도록 변경한 것이다. 이로써 우리의 직관을 따르고 변경에 용이한 코드가 만들어
졌다.
수정 전에는 Theater가 Audience와 TicketSeller의 상세한 내부 구현까지 알아야했고, 강하게 3객체가 결합되었다. 사소한 변경에도 Theater는 영향을 많이 
받았다.

수정 후 Theater는 Bag이나 TicketOffice에 대해 알지 못한다. 다른 객체가 TicketOffice에서 맘대로 휘젓고 다닐 수 없게 됐다. 즉, 객체 자율성을 높이는
방향으로 설계 했는데, 이 결과로 유연하고 이해하기 쉬운 코드를 얻을 수 있었다. 

### 캡슐화와 응집도

개선의 핵심은 캡슐화하고 객체 간에 오직 메시지를 통해서만 상호작용하도록 만드는 것이다. Theater는 TicketSeller의 내부에 대해서는 전혀 알지 못한다. 
TicketSeller 또한 Audience내부를 알지 못한다. 서로 메시지에 응답하고 원하는 결과를 반환한다 정도만 알 수 있다. 밀접하게 연관된 작업만을 수행하고 연관성
없는 작업은 다른 객체에게 위임하는 객체를 가리켜 응집도가 높다고 말한다. 자신의 데이터를 스스로 처리하는 자율적인 객체를 만들면 결합도를 낮추고 응집도를 높일 수
있다.

### 절차지향과 객체지향

수정하기 전의 코드에서는 Theater의 enter 메서드 안에서 Audience와 TicketSeller로부터 Bag과 TicketOffice를 가져와 관람객을 입장시키는 절차를 
구현했다. 

이 관점에서 Theater의 enter는 Process이며 Audience, TicketSeller, Bag, TicketOffice는 Data다. 이 처럼 프로세스와 데이터를 별도의 모듈에
위치 시키는 방식을 절차적 프로그래밍이라한다. 

절차적 프로그래밍 방식은 다음과 같은 단점이 존재한다. 

1. 모든 객체들이 의존적이기 때문에 작은 변화에도 많은 객체들이 영향을 받는다. 
2. 우리의 직관에 위배된다. 절차적 프로그래밍에서 판매원과 관람객은 수동적인 존재이다. 코드를 읽는 사람과 원활하게 의사소통하지 못한다.
3. 가장 큰 문제는 결합도가 높고 변경은 버그를 부른다, 그리고 이 버그에 의한 두려움은 코드를 변경하기 어렵게 만든다. 

해결방법은 자신의 데이터를 스스로 처리하도록 데이터와 프로세스 모두 동일한 모듈 내부에 위치하도록 객체지향 프로그래밍 방식으로 설계하는 것이다.

객체 사이의 결합도를 낮추기 때문에 절차지향에 비해 변경에 유연하며 자기 자신의 문제를 객체 스스로 처리하니 이해하기 쉬워진다. 객체 내부의 변경 또한 객체 스스로 
처리하고 있으니 하나의 객체만 변경해주면 된다. 

### 책임의 이동

Theater에 모든 책임이 집중되었던 수정 전 코드와는 반대로 수정 후 여러 객체에 걸쳐 기능에 필요한 책임이 분산돼 있었다. 결론적으로 Theater에 몰려있던 책임이
각각의 객체로 이동 된 것이다. 
사실 객체지향의 핵심은 객체에 적절한 책임을 할당하는 것이다. 객체는 다른 객체와 협력이라는 문맥 안에서 특정한 역할을 수행하는 데 필요한 적절한  책임을 수행해야
한다.

### 추가 개선

Bag을 자율적인 존재로 수정하기

```java
public class Bag {
    private Long amount;
    private Invitation invitation;
    private Ticket ticket;

    public Long hold(final Ticket ticket) {
        if (this.hasInvitation()) {
            this.addTicket(ticket);
            return 0L;
        }

        addTicket(ticket);
        minusAmount(ticket.getFee());
        return ticket.getFee();
    }

    public boolean hasInvitation() {
        return invitation != null;
    }

    public void addTicket(final Ticket ticket) {
        this.ticket = ticket;
    }

    public void minusAmount(final Long amount) {
        this.amount -= amount;
    }
}
```

```java
public class Audience {
    private Bag bag;

    public Audience(Bag bag) {
        this.bag = bag;
    }

    public boolean hasTicket() {
        return this.bag.hasTicket();
    }

    public Long buy(final Ticket ticket) {
        return this.bag.hold(ticket);
    }
}
```

TicketOffice를 자율적인 객체로 개선하기 

```java
public class TicketOffice {
    private Long amount;
    private List<Ticket> tickets = new ArrayList<>();

    public TicketOffice(Long amount, Ticket... tickets) {
        this.amount = amount;
        this.tickets.addAll(Arrays.asList(tickets));
    }

    public void sellTicketTo(final Audience audience) {
        plusAmount(audience.buy(this.getTicket()));
    }

    public Ticket getTicket() {
        return tickets.remove(0);
    }

    public void minusAmount(final Long amount) {
        this.amount -= amount;
    }

    public void plusAmount(final Long amount) {
        this.amount += amount;
    }
}
```

```java
public class TicketSeller {
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public void sellTo(Audience audience) {
        ticketOffice.sellTicketTo(audience);
    }
}
```

TicketOffice와 Audience사이에 새로운 의존성이 추가 되어 Audience를 TicketOffice에서 알아야한다. 의존성이 추가되면 높은 결합도를 의미하고, 높은 결합
도는 변경하기 어려운 설계를 의미한다. TicketOffice의 자율성은 높였지만 전체 설계 관점에서는 결합도가 상승했다. 

이는 트레이드 오프를 고려하여야 한다. 어떤 것이 우선일까? 사람에 따라 누군 자율성을 만족시킬 것이고 누군 결합도를 낮추려고 할 것이다. 그렇다 어떤 기능을 설계하든
방법은 한 가지 이상일 수 있고 설계는 트레이드 오프의 산물이다. 

누구도 만족시킬 수 없다.

### 그래 거짓말이다!

TicketOffice와 Bage은 현실세계에서 자유로운 존재가 아니다. 하지만 우리는 위에서 이들을 생물처럼 다뤘다. 객체지향에서는 모든 것이 능동적이고 자율적인 존재이기
때문에 가능하다. 
앞에서는 실세계에서의 생물처럼 스스로 생각하고 행동하도록 소프트웨어 객체를 설계 하는 것이 이해하기 쉬운 코드를 작성하는 것이라고 설명했다. 하지만 훌륭한 소프트웨어는
모든 객체들이 자율적으로 행동해야한다. 따라서 이해하기 쉬운 코드를 작성하고 싶다면 차라리 한 편의 애니메이션을 만든다고 생각하라.

### 객체지향 설계

좋은 설계란 요구사항을 만족하면서 변화에 유연한 코드를 설계하는 것이다. 객체지향 프로그래밍은 우리에게 이러한 다양한 방법을 제공해준다. 객체지향 프로그래밍은 이해하기
쉬운 코드를 작성할 수 있게 돕고 두 객체를 결합을 제거하고 응집성을 높이자.