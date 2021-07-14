package transform;

public class Account {
	private float balance = 0;

	public Account() {
	}

	public void deposit(Money amount) {
		this.balance = this.balance + amount.dollars + amount.cents;
	}

	public float getBalance() {
		return this.balance;
	}
}
