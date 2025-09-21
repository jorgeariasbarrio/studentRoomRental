<template>
  <ul class="nav nav-tabs" style="margin-top: 3%">
    <li class="nav-item">
      <a class="nav-link" href="/miPerfil">Información perfil</a>
    </li>
    <li class="nav-item">
      <a class="nav-link active" aria-current="page" href="/reviews"
        >Mis reviews</a
      >
    </li>
  </ul>
  <br />
  <div style="width: 80%" v-for="review in reviews" :key="review.id">
    <ReviewCard :review="review" :habitacion="review.habitacion"></ReviewCard>
  </div>
  <div v-if="reviews.length === 0" style="margin-top: 5%">
    <h6>No tienes ninguna por el momento!</h6>
  </div>
</template>

<script>
import ReviewRepository from "@/repositories/ReviewRepository";
import AccountRepository from "@/repositories/AccountRepository";
import ReviewCard from "@/components/ReviewCard";

export default {
  name: "MiPerfil",
  data() {
    return {
      reviewsPendientes: [],
      reviewsCompletadas: [],
      reviews: [],
    };
  },
  components: { ReviewCard },
  mounted() {
    this.getReviews();
  },
  methods: {
    async getReviews() {
      let account = await AccountRepository.getAccount();
      this.reviews = await ReviewRepository.getReviewsByUser(account.id);
    },
  },
};
</script>

<style scoped></style>
