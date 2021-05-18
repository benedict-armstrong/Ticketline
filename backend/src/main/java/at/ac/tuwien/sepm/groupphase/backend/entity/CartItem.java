package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import java.util.Objects;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationUser user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER)
    private SectorType sectorType;

    @Column(nullable = false)
    private Integer amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public SectorType getSectorType() {
        return sectorType;
    }

    public void setSectorType(SectorType sectorType) {
        this.sectorType = sectorType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id)
            && Objects.equals(user, cartItem.user)
            && Objects.equals(event, cartItem.event)
            && Objects.equals(sectorType, cartItem.sectorType)
            && Objects.equals(amount, cartItem.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, event, sectorType, amount);
    }

    @Override
    public String toString() {
        return "CartItem{"
            + "id=" + id
            + ", user=" + user
            + ", event=" + event
            + ", sectorType=" + sectorType
            + ", amount=" + amount
            + '}';
    }

    public static final class CartItemBuilder {
        private Long id;
        private ApplicationUser user;
        private Event event;
        private SectorType sectorType;
        private Integer amount;

        private CartItemBuilder() {
        }

        public static CartItem.CartItemBuilder aCartItem() {
            return new CartItem.CartItemBuilder();
        }

        public CartItem.CartItemBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CartItem.CartItemBuilder withUser(ApplicationUser user) {
            this.user = user;
            return this;
        }

        public CartItem.CartItemBuilder withEvent(Event event) {
            this.event = event;
            return this;
        }

        public CartItem.CartItemBuilder withSectorType(SectorType sectorType) {
            this.sectorType = sectorType;
            return this;
        }

        public CartItem.CartItemBuilder withAmount(Integer amount) {
            this.amount = amount;
            return this;
        }

        public CartItem build() {
            CartItem cartItem = new CartItem();
            cartItem.setId(id);
            cartItem.setUser(user);
            cartItem.setEvent(event);
            cartItem.setSectorType(sectorType);
            cartItem.setAmount(amount);
            return cartItem;
        }
    }

}
