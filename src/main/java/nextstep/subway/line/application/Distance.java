package nextstep.subway.line.application;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
  @Column
  private int distance;

  public Distance() {
  }

  public Distance(int distance) {
    this.distance = distance;
  }

  public static Distance of(int distance) {
    return new Distance(distance);
  }

  public Distance plus(Distance newDistance) {
    return of(distance += newDistance.distance);
  }

  public Distance minus(Distance newDistance) {
    if (this.distance <= newDistance.distance) {
      throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    return of(distance -= newDistance.distance);
  }

  public int getDistance() {
    return distance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Distance distance1 = (Distance) o;
    return distance == distance1.distance;
  }

  @Override
  public int hashCode() {
    return Objects.hash(distance);
  }
}
